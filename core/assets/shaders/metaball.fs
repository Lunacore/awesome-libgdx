#version 120

varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform vec3[256] metaballs;
uniform vec3[256] metacolors;

uniform float threshold;

uniform vec2 viewport;
uniform vec2 cameraPosition;
uniform float cameraZoom;


void main()
{

	vec2 uv = v_texCoords*viewport;
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	float influence = 0;
	
	float cont = 0.0;

	for(int i = 0; i < metaballs.length; i ++){
		vec3 transformed = vec3((metaballs[i].xy - cameraPosition) / cameraZoom + viewport/2.0, metaballs[i].z / cameraZoom);
	
		float len = length(uv - transformed.xy) / viewport.x;
		
		// DESCOBRIR UMA FUNÇÃO DE FALLOFF MELHOR QUE ESSA (já tentai gaussiana, e umas parada maluca ai)
		
		float addup = (transformed.z / pow(len, 2.0)) / 100.0;
		float coloraddup = -len / transformed.z + 1.0;
		
		if(transformed.z > 0.0 && addup > 10){
			color += vec4(metacolors[i] * coloraddup, 1.0);
			influence += addup;
			cont += 1.0;
		}

	}
	
	color /= cont;
	
	color.a = 1.0;
	
	if(influence < threshold){
		color = vec4(0.0, 0.0, 0.0, 0.0);
	}
	
	

    gl_FragColor = color;
}