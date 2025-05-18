package Controllers;

import java.io.StringReader;
import java.io.BufferedReader;

class MockServletInputStream extends jakarta.servlet.ServletInputStream {
    private final String content;
    private final BufferedReader reader;
    
    public MockServletInputStream(String content) {
        this.content = content;
        this.reader = new BufferedReader(new StringReader(content));
    }
    
    @Override
    public int read() throws java.io.IOException {
        return reader.read();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(jakarta.servlet.ReadListener readListener) {
        // Implementação vazia para teste
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}