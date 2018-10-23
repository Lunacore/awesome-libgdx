varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float intensity;
uniform float frequency;
uniform float speed;
uniform float time;

float lerp(float a, float b, float alpha){
	return a + alpha * (b - a);
}

void main()
{


    vec2 uv = v_texCoords;

    vec2 newuv = vec2(uv.x, uv.y + sin((lerp(0.0, 3.1415*2.0, uv.x) + time*speed)*frequency) * intensity);

    gl_FragColor = texture2D(u_texture, newuv);
}