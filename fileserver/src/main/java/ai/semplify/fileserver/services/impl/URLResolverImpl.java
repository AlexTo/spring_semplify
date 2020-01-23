package ai.semplify.fileserver.services.impl;

import ai.semplify.fileserver.services.URLResolver;
import org.springframework.stereotype.Service;

@Service
public class URLResolverImpl implements URLResolver {
    @Override
    public String resolve(Long fileId, String module) {
        return "/files/" + fileId.toString();
    }
}
