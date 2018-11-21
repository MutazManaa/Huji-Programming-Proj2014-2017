#version 330

// snoise and turb from here:
// http://glsl.heroku.com/e#812.1

vec3 mod289(vec3 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 mod289(vec4 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 permute(vec4 x) {
	return mod289(((x*34.0)+1.0)*x);
}

vec4 taylorInvSqrt(vec4 r)
{
	return 1.79284291400159 - 0.85373472095314 * r;
}

float snoise(vec3 v)
{
	const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;
	const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);

	// First corner
	vec3 i  = floor(v + dot(v, C.yyy) );
	vec3 x0 =   v - i + dot(i, C.xxx) ;

	// Other corners
	vec3 g = step(x0.yzx, x0.xyz);
	vec3 l = 1.0 - g;
	vec3 i1 = min( g.xyz, l.zxy );
	vec3 i2 = max( g.xyz, l.zxy );

	//   x0 = x0 - 0.0 + 0.0 * C.xxx;
	//   x1 = x0 - i1  + 1.0 * C.xxx;
	//   x2 = x0 - i2  + 2.0 * C.xxx;
	//   x3 = x0 - 1.0 + 3.0 * C.xxx;
	vec3 x1 = x0 - i1 + C.xxx;
	vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y
	vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y

	// Permutations
	i = mod289(i);
	vec4 p = permute( permute( permute(
									   i.z + vec4(0.0, i1.z, i2.z, 1.0 ))
							  + i.y + vec4(0.0, i1.y, i2.y, 1.0 ))
					 + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));

	// Gradients: 7x7 points over a square, mapped onto an octahedron.
	// The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)
	float n_ = 0.142857142857; // 1.0/7.0
	vec3  ns = n_ * D.wyz - D.xzx;

	vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)

	vec4 x_ = floor(j * ns.z);
	vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)

	vec4 x = x_ *ns.x + ns.yyyy;
	vec4 y = y_ *ns.x + ns.yyyy;
	vec4 h = 1.0 - abs(x) - abs(y);

	vec4 b0 = vec4( x.xy, y.xy );
	vec4 b1 = vec4( x.zw, y.zw );

	//vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;
	//vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;
	vec4 s0 = floor(b0)*2.0 + 1.0;
	vec4 s1 = floor(b1)*2.0 + 1.0;
	vec4 sh = -step(h, vec4(0.0));

	vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;
	vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;

	vec3 p0 = vec3(a0.xy,h.x);
	vec3 p1 = vec3(a0.zw,h.y);
	vec3 p2 = vec3(a1.xy,h.z);
	vec3 p3 = vec3(a1.zw,h.w);

	//Normalise gradients
	vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));
	p0 *= norm.x;
	p1 *= norm.y;
	p2 *= norm.z;
	p3 *= norm.w;

	// Mix final noise value
	vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
	m = m * m;
	return 42.0 * dot( m*m, vec4(dot(p0,x0), dot(p1,x1),
								 dot(p2,x2), dot(p3,x3) ) );
}

float turb(vec3 v)
{
	const float base_freq = 0.2; // SPIDER

	vec4 noisevec;
	noisevec.x = snoise(v * base_freq*1.0) * 8.0;
	noisevec.y = snoise(v * base_freq*2.0) * 4.0;
	noisevec.z = snoise(v * base_freq*4.0) * 2.0;
	noisevec.w = snoise(v * base_freq*8.0) * 1.0;
	// noisevec = (noisevec / 8.0 + 1.0) / 2.0;
	noisevec = noisevec / 8.0;
	// noisevec = noisevec * noisevec;

	float intensity = abs(noisevec[0] - 0.20) +
	abs(noisevec[1] - 0.10) +
	abs(noisevec[2] - 0.05) +
	abs(noisevec[3] - 0.025);
	return intensity;
}


#define PHONG 1
#define MARBLE 1
#define WOOD 2
#define MIRROR 3
#define CUBE 4
#define REGULAR 0
#define PI 3.14
#define EPSILON 0.001
uniform mat4 projection;
uniform mat4 translate;
uniform mat4 rotate;


uniform float secondLight = 1;
uniform float specExp;
uniform vec3 lightColor1 = vec3(1.0, 0.9, 0.7); // Firts light colour
uniform vec3 lightColor2 = vec3(0.6, 0.6, 1.0); // Second light colour
uniform vec3 ambientColor = vec3(1.0, 1.0, 1.0);// Ambient light color
uniform vec3 light_position1 = vec3(  3.0, 2.0,  1.0); // Firts light position
uniform vec3 light_position2 = vec3( -3.0, 0.0,  1.0); // Second light position
uniform vec3 ka = vec3(0.1, 0.1, 0.1); // Ambient coefficient
uniform vec3 kd = vec3(0.3, 0.3, 0.3); // Diffuse coefficient
uniform vec3 ks = vec3(0.3, 0.3, 0.3); // Specular coefficient
uniform float colourModel;

in vec4 shaderVertexcolour;
in vec3 shadingPosition;
in vec3 tempnormal;
in vec3 normalPosition;

uniform sampler2D textureMap;
uniform sampler2D textureBump;
uniform sampler2D textureSphere;

uniform float texturingMode;
uniform float textureScale;
uniform float turbulenceCoeff;

out vec4 outColor;

void main()
{
    vec3 kd = vec3(0.0);

	if(colourModel == PHONG)
	{
		//shading with 2 light sources

		vec3 light_dir1 = normalize(shadingPosition - light_position1);
		vec3 light_dir2 = normalize(shadingPosition - light_position2);
		vec3 toEye = normalize(vec3(0.0, 0.0, 3.0) - shadingPosition);
		vec3 nnormal = normalize(tempnormal);

		vec3 myKS;
        vec3 envmapColor = vec3(0.0, 0.0, 0.0);

        //withou texturing
        if(texturingMode == REGULAR)
        {
            myKS = ks;
            kd = vec3(0.3, 0.3, 0.3);
            toEye = normalize(vec3(0.0, 0.0, 3.0) - shadingPosition);

        }else if(texturingMode == MARBLE)
        {
            vec3 texturePosition = textureScale * normalPosition;

            float fmarble = turb(texturePosition);
            float a = turbulenceCoeff;
            myKS = ks;
            float k = sin(2 * PI * (texturePosition.x + a*fmarble));
            kd = 0.4 * (1 + vec3(k * k * k));

        }else if(texturingMode == WOOD)
        {
            vec3 darkWoodColor = vec3(110/255.0, 55/255.0, 15/255.0);
            vec3 lightWoodColor = vec3(185/255.0,120/255.0,55/255.0);
            vec3 texturePosition = textureScale * normalPosition;

            float a = turbulenceCoeff;
            float d = length(texturePosition.yz) + a * turb(texturePosition.xyz);
            float fwood = abs(cos(2 * PI * fract(d)));

            kd = mix(darkWoodColor, lightWoodColor, fwood);
            myKS = 0.1*ks;

        }else if(texturingMode == MIRROR)
        {
            vec2 coordTextura;
            vec3 nreflected = normalize(reflect(-toEye,nnormal));


            float theta = atan(nreflected.x, nreflected.z);
            float phi   = atan(nreflected.y, length(nreflected.xz));

            coordTextura.x = (theta + PI) / (2*PI);
            coordTextura.y = 1.0 - (phi + PI/2) / PI;
            envmapColor = texture(textureSphere, coordTextura).xyz;

            kd = vec3(0.0, 0.0, 0.0);
            myKS = vec3(0.0, 0.0, 0.0);

        }else if(texturingMode == CUBE)
        {

            float textura = 0;
            float dBdu = 0;
            float dBdv = 0;
            float deltaU = 1.f/512.f;
            float deltaV = 1.f/512.f;
            float bumpScale = 0.2*textureScale;

            vec3 texturePosition = normalPosition;
            vec3 p = abs(normalPosition);

            if (p.x > 1 - EPSILON || p.x < EPSILON)
                {
            	textura = texture(textureBump, texturePosition.zy).r;
            	dBdu = texture(textureBump, vec2(texturePosition.z + deltaU, texturePosition.y)).r - textura;
            	dBdv = texture(textureBump, vec2(texturePosition.z, texturePosition.y + deltaV)).r - textura;
            	nnormal = normalize(nnormal - bumpScale*vec3(dBdu, dBdv, 0.0));
            	kd = texture(textureMap, texturePosition.zy).xyz;
            	}

            if (p.y > 1 - EPSILON || p.y < EPSILON)
                {
            	textura = texture(textureBump, texturePosition.xz).r;
            	dBdu = texture(textureBump, vec2(texturePosition.x + deltaU, texturePosition.z)).r - textura;
            	dBdv = texture(textureBump, vec2(texturePosition.x, texturePosition.z + deltaV)).r - textura;
            	nnormal = normalize(nnormal - bumpScale*vec3(dBdu, dBdv, 0.0));
            	kd = texture(textureMap, texturePosition.xz).xyz;
            	}

            if (p.z > 1 -EPSILON || p.z < EPSILON)
                {
            	textura = texture(textureBump, texturePosition.xy).r;
            	dBdu = texture(textureBump, vec2(texturePosition.x + deltaU, texturePosition.y)).r - textura;
            	dBdv = texture(textureBump, vec2(texturePosition.x, texturePosition.y + deltaV)).r - textura;
            	nnormal = normalize(nnormal - bumpScale*vec3(dBdu, dBdv, 0.0));
            	kd = texture(textureMap, texturePosition.xy).xyz;
            	}

            	myKS = vec3(0.0, 0.0, 0.0);

        }


		    vec3 diffColor = ka * ambientColor + lightColor1 * kd * max(dot(nnormal, -light_dir1), 0.0) + lightColor2 * kd * max(dot(nnormal, -light_dir2), 0.0) * secondLight;
		    vec3 r1 = normalize(reflect(light_dir1, nnormal));
		    vec3 r2 = normalize(reflect(light_dir2, nnormal));
		    vec3 specularColor = myKS * pow(max(dot(toEye, r1), 0.0), specExp) * lightColor1 + myKS * pow(max(dot(toEye, r2), 0.0), specExp) * lightColor2;


		    outColor = vec4(diffColor + specularColor + envmapColor, 1.0f);


	}
	//The other colouring modes(regular as part 1 or guard we don't add texturing or do anyThing special
	else
	{
		outColor = shaderVertexcolour;
	}
}