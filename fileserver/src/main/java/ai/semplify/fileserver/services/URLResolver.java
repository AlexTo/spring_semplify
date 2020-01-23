package ai.semplify.fileserver.services;

public interface URLResolver {
    String resolve(Long fileId, String module);
}
