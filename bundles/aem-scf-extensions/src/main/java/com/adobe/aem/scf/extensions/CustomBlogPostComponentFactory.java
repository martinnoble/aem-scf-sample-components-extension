package com.adobe.aem.scf.extensions;

import com.adobe.cq.social.commons.comments.api.Comment;
import com.adobe.cq.social.commons.comments.listing.CommentSocialComponentListProviderManager;
import com.adobe.cq.social.journal.client.api.JournalSocialComponentFactory;
import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.QueryRequestInfo;
import com.adobe.cq.social.scf.SocialComponent;
import com.adobe.cq.social.scf.SocialComponentFactory;
import com.adobe.cq.social.scf.core.AbstractSocialComponentFactory;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

/**
 * CustomCommentFactory extends the default CommentSocialComponentFactory to leverage the default comment social
 * component implementation. This makes it possible to only make changes needed for customization without having to
 * implement all the APIs specified by {@link Comment}.
 */
@Component(name = "Custom Blog Post Social Component Factory")
@Service
public class CustomBlogPostComponentFactory extends AbstractSocialComponentFactory implements SocialComponentFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CustomBlogPostComponentFactory.class);


    @Reference
    private CommentSocialComponentListProviderManager commentListProviderManager;

    @Override
    public SocialComponent getSocialComponent(final Resource resource) {
        try {
            LOG.info("POST Factory - returning 1");
            return new CustomBlogPostComponent(resource, this.getClientUtilities(resource.getResourceResolver()),commentListProviderManager);
        } catch (RepositoryException e) {
            return null;
        }
    }

    @Override
    public SocialComponent getSocialComponent(final Resource resource, final SlingHttpServletRequest request) {
        try {
            LOG.info("POST Factory - returning 2");
            return new CustomBlogPostComponent(resource, this.getClientUtilities(request),this.getQueryRequestInfo(request),commentListProviderManager);
        } catch (RepositoryException e) {
            return null;
        }
    }

    @Override
    public SocialComponent getSocialComponent(Resource resource, ClientUtilities clientUtils, QueryRequestInfo listInfo) {
        try {
            LOG.info("POST Factory - returning 3");
            return new CustomBlogPostComponent(resource, clientUtils, listInfo,commentListProviderManager);
        } catch (RepositoryException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.adobe.cq.social.commons.client.api.AbstractSocialComponentFactory#getPriority() Set the priority to a
     * number greater than 0 to override the default SocialComponentFactory for comments.
     */
    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public String getSupportedResourceType()
    {
        //return "acme/components/blog/post";
        return "disabled";
    }

}
