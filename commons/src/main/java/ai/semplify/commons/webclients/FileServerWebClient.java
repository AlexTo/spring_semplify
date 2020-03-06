package ai.semplify.commons.webclients;

public interface FileServerWebClient {
    byte[] download(Long fileId);
}
