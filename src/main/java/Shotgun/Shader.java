package Shotgun;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    int vertexID;
    int fragmentID;
    int shaderProgram;

    String vertexSource;
    String fragmentSource;
    String shaderPath;

    boolean inUse;


    public Shader(String shaderPath) {
        this.shaderPath = shaderPath;

        try {
            String shaderSource = new String(Files.readAllBytes(Paths.get(shaderPath)));
            String[] splitString = shaderSource.split("#type ");
            for (int i = 1; i < splitString.length; i++) {
                // Get the type that is next to "#type"
                int endOfType = splitString[i].indexOf("\r\n") + 2;
                String type = splitString[i].substring(0, endOfType).trim();
//                System.out.println(type);
                if (type.equals("vertex")) {
                    // Set source as Vertex shader
                    vertexSource = splitString[i].substring(endOfType);
                } else if (type.equals("fragment")) {
                    // Set source as Fragment shader
                    fragmentSource = splitString[i].substring(endOfType);
                } else {
                    // Throw error
                    System.err.printf("Error: %s is not recognized as a type!%n", type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "";
        }
    }

    public void compile() {

        // ===========================
        // Compile and link shaders
        // ===========================

        // First load and compile vertex Shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader src to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        // Check for errors during compilation
        int succes = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + shaderPath + "' \n \t VertexShader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader src to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        // Check for errors during compilation
        succes = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + shaderPath + "' \n \t FragmentShader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for errors during linking
        succes = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + shaderPath + "' \n \t Shaderprogram linking failed.");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
        }

    }

    public void use() {
        if (!inUse) {
            // Bind the shader program
            glUseProgram(shaderProgram);
            inUse = true;
        }
    }

    public void detach() {
        if (inUse) {
            glUseProgram(0);
            inUse = false;
        }
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform3f(varLocation, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec2f(String varName, Vector2f vec2f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, val);
    }
}
