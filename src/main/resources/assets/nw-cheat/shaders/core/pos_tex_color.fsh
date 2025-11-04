#version 330 core

uniform sampler2D u_Texture;

in vec4 v_Color;
in vec2 v_TexCoord;

out vec4 color;

void main() {
    color = texture(u_Texture, v_TexCoord) * v_Color;

    if (color.a == 0.0) {
        discard;
    }
}