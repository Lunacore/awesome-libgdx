package com.mygdx.game.objects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class TmxRenderer{
	
	private TiledMap tiledMap;
	Vector2 scaleVector;
	Box2DMapObjectParser parser;
	HashMap<Integer, GameObject> instancedObjects;
	ArrayList<TmxInstancedKeyword> keywords;
	ObjectInfo info;
	
	public TmxRenderer(ObjectInfo info, String mapPath) {
		this.info = info;
		parser = new Box2DMapObjectParser(info.getScale()/State.PHYS_SCALE);
		instancedObjects = new HashMap<Integer, GameObject>();
		keywords = new ArrayList<TmxInstancedKeyword>();
		loadDefaultKeywords();
		setTiledMap(new TmxMapLoader().load(mapPath));
		scaleVector = new Vector2(info.getScale() / State.PHYS_SCALE, info.getScale() / State.PHYS_SCALE);
		Iterator<MapLayer> layers = getTiledMap().getLayers().iterator();
		while(layers.hasNext()) {
			MapLayer layer = layers.next();
			
			parser.load(info.getState().getWorld(), layer);
		
			if(layer.getProperties().get("block") == null) continue;
			if(layer.getProperties().get("block", Boolean.class)) {
				
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

				Vector2 tileSize = new Vector2(tileLayer.getTileWidth(), tileLayer.getTileHeight());
					
				for(int x = 0; x < tileLayer.getWidth(); x ++) {
					for(int y = 0; y < tileLayer.getHeight(); y ++) {
						Cell cell = tileLayer.getCell(x, y);
						if(cell != null) {
							Vector2 pos = new Vector2(
									(x * tileSize.x) * info.getScale(),
									(y * tileSize.y) * info.getScale()
									);
							info.getState().addStaticRectangleBody(pos.cpy().add(tileSize.cpy().scl(1/2f)), tileSize.cpy().scl(info.getScale()));
						}
					}
					
				}
			}
		}
	}
	
	public MapProperties trataProps(MapProperties props, MapObject mo, MapLayer layer) {
			MapProperties newProps = new MapProperties();
		
			Iterator<String> it = props.getKeys();
			while(it.hasNext()) {
				String key = it.next();
				
				if(props.get(key) instanceof String) {
				String value = props.get(key, String.class);
				
					for(TmxInstancedKeyword tik : keywords) {
						if(value.startsWith(tik.getKeyword())) {
							if(value.endsWith("_")) {
								String objectName = value.split("_")[1];
								Object nOb = tik.getObject(layer.getObjects().get(objectName));
								if(nOb == null) {
									Gdx.app.exit();
								}
								newProps.put(key, nOb);
								break;
							}
							else {
								newProps.put(key, tik.getObject(mo));
								break;
							}
						}
						else {
							newProps.put(key, value);
						}
					}
					
				}
				else {
					
					Object value = props.get(key);
					
					if(key.equals("x")) value = props.get("x", Float.class) * info.getScale();
					if(key.equals("y")) value = props.get("y", Float.class) * info.getScale();
					if(key.equals("width")) value = props.get("width", Float.class) * info.getScale();
					if(key.equals("height")) value = props.get("height", Float.class) * info.getScale();
					
					
					newProps.put(key, value);
				}
				
				
			}
		
		return newProps;
	}
	
	public void instanceSingle(MapObject mo, MapLayer layer, int layerCount) {
		
		MapProperties props = mo.getProperties();
		String objClass = props.get("class", String.class);
			
		
			try {
				//Pega a classe que vai ser instanciada
				@SuppressWarnings("rawtypes")
				Class goClass = Class.forName(objClass);
				
				@SuppressWarnings("unchecked")
				GameObject go = (GameObject) goClass.getConstructor(ObjectInfo.class, MapProperties.class).newInstance(
						new ObjectInfo(info.getState(),
								props.get("z") != null ? props.get("z", Integer.class) : layerCount,
								info.getScale()
								),
						trataProps(props, mo, layer)
						);
				
				info.getState().putInScene(go);
				instancedObjects.put(mo.getProperties().get("id", Integer.class), go);
				
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				System.err.println("Erro ao tentar instanciar objeto da classe " + objClass);
				e.getTargetException().printStackTrace();
		
				Gdx.app.exit();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		
		
	}
	
	public void instanceImage(MapObject mo, MapLayer layer, int layerCount) {

		TiledImageObject io = new TiledImageObject(new ObjectInfo(info.getState(), layerCount, info.getScale()), (TiledMapTileMapObject) mo);
		info.getState().putInScene(io);
		instancedObjects.put(mo.getProperties().get("id", Integer.class), io);

		
	}
	
	public void instanceObjects() {
		Iterator<MapLayer> layers = getTiledMap().getLayers().iterator();
		int layerCount = 0;
		while(layers.hasNext()) {
			
			MapLayer layer = layers.next();
			MapObjects mos = layer.getObjects();
			
			for(int k = 0; k < mos.getCount(); k ++) {
				MapProperties props =  mos.get(k).getProperties();

				String objClass = props.get("class", String.class);
				if(objClass != null) {
					try {
						instanceSingle(mos.get(k), layer, layerCount);
					}
					catch(Exception e) {
						System.err.println("Erro ao instanciar objeto da classe " + objClass);
						//Gdx.app.exit();
					}
				}
				else if(mos.get(k) instanceof TiledMapTileMapObject){
					instanceImage(mos.get(k), layer, layerCount);
				}
			}
			
			layerCount ++;
		}
		
		for(int g : instancedObjects.keySet()) {
			instancedObjects.get(g).create();
		}
	}
	
	public void loadDefaultKeywords() {
		addKeywordInterpreter(new TmxInstancedKeyword("{world}") {
			public Object getObject(MapObject mo) {
				return info.getState().getWorld();
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{position}") {
			public Object getObject(MapObject mo) {
				return new Vector2(mo.getProperties().get("x", Float.class) * info.getScale(), mo.getProperties().get("y", Float.class) * info.getScale());
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{rotation}") {
			public Object getObject(MapObject mo) {
				return mo.getProperties().get("angle", Float.class);
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{size}") {
			public Object getObject(MapObject mo) {
				return new Vector2(mo.getProperties().get("width", Float.class) * info.getScale(), mo.getProperties().get("height", Float.class) * info.getScale());
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{state}") {
			public Object getObject(MapObject mo) {
				return info.getState();
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{this}") {
			public Object getObject(MapObject mo) {
				return mo;
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{scale}") {
			public Object getObject(MapObject mo) {
				return info.getScale();
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{null}") {
			public Object getObject(MapObject mo) {
				return null;
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{body}") {
			public Object getObject(MapObject mo) {
				return parser.getBodies().get(mo.getName());
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{center}") {
			public Object getObject(MapObject mo) {
				return new Vector2(
						(mo.getProperties().get("x", Float.class) + mo.getProperties().get("width", Float.class)/2) * info.getScale(),
						(mo.getProperties().get("y", Float.class) + mo.getProperties().get("height", Float.class)/2) * info.getScale()
						);
			}
		});

	}
	
	public void addKeywordInterpreter(TmxInstancedKeyword tik) {
		keywords.add(tik);
	}
	
	public GameObject getInstancedObject(Integer i) {
		return instancedObjects.get(i);
	}

	Vector2 positionTemp = new Vector2(0, 0);
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

		for(int i = 0; i < getTiledMap().getLayers().getCount(); i ++) {
			MapLayer layer = getTiledMap().getLayers().get(i);
			
			if(layer instanceof TiledMapTileLayer) {
				
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

				Vector2 tileSize = new Vector2(tileLayer.getTileWidth(), tileLayer.getTileHeight());
					
				//Desenha os tiles
				sb.begin();
				for(int x = 0; x < tileLayer.getWidth(); x ++) {
					for(int y = 0; y < tileLayer.getHeight(); y ++) {
						Cell cell = tileLayer.getCell(x, y);
						if(cell != null) {
							positionTemp.set(
									(x * tileSize.x) * info.getScale() / State.PHYS_SCALE,
									(y * tileSize.y) * info.getScale() / State.PHYS_SCALE
									);
							
							
							Helper.renderRegion(
									sb,
									cell.getTile().getTextureRegion(),
									positionTemp.cpy().add(tileSize.cpy().scl(1/2f / State.PHYS_SCALE)),
									cell.getRotation(),
									scaleVector,
									cell.getFlipHorizontally(),
									cell.getFlipVertically());

						}
					}
					
				}
				sb.end();
			}
		}
	}
	
	public Vector2 getPositionFromObject(String objectName) {
		for(int i = 0; i < getTiledMap().getLayers().getCount(); i ++) {
			MapLayer layer = getTiledMap().getLayers().get(i);
			MapObject obj = layer.getObjects().get(objectName);
			if(obj != null) {
				RectangleMapObject posObj = (RectangleMapObject) obj;
				Vector2 position = new Vector2();
				posObj.getRectangle().getPosition(position);
				return position.scl(info.getScale());
			}
		}
		
		return null;
	}

	public boolean update(float delta) {
		return false;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public void setTiledMap(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
	}

}
