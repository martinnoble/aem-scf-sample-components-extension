package com.adobe.aem.scf.extensions;

import com.adobe.cq.social.commons.comments.listing.CommentSocialComponentListProviderManager;
import com.adobe.cq.social.forum.client.api.AbstractPost;
import com.adobe.cq.social.forum.client.api.ForumConfiguration;
import com.adobe.cq.social.forum.client.api.Post;
import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.QueryRequestInfo;
import org.apache.sling.api.resource.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

public class CustomBlogPostComponent extends AbstractPost<ForumConfiguration> implements Post<ForumConfiguration> {


    private static final Logger LOG = LoggerFactory.getLogger(CustomBlogPostComponent.class);

    /**
     * Construct a comment for the specified resource and client utilities.
     *
     * @param resource                   the specified resource
     * @param clientUtils                the client utilities instance
     * @param commentListProviderManager list manager to use for listing content
     * @throws RepositoryException if an error occurs
     */
    public CustomBlogPostComponent(final Resource resource, final ClientUtilities clientUtils,
                                   final CommentSocialComponentListProviderManager commentListProviderManager) throws RepositoryException {
        super(resource, clientUtils, commentListProviderManager);

        LOG.info("BLOG POST - created 1");
    }

    /**
     * Constructor of a comment.
     *
     * @param resource                   the specified {@link com.adobe.cq.social.commons.Comment}
     * @param clientUtils                the client utilities instance
     * @param queryInfo                  the query info.
     * @param commentListProviderManager list manager to use for listing content
     * @throws RepositoryException if an error occurs
     */
    public CustomBlogPostComponent(final Resource resource, final ClientUtilities clientUtils,
                                   final QueryRequestInfo queryInfo, final CommentSocialComponentListProviderManager commentListProviderManager)
            throws RepositoryException {
        super(resource, clientUtils, queryInfo, commentListProviderManager);
        LOG.info("BLOG POST - created 2");

    }

    public CustomBlogPostComponent(final Resource resource, final ClientUtilities clientUtils, final QueryRequestInfo queryInfo,
                                   final Resource latestPost, final int numReplies,
                                   final CommentSocialComponentListProviderManager listProviderManager) throws RepositoryException {
        super(resource, clientUtils, queryInfo, latestPost, numReplies, listProviderManager);
        LOG.info("BLOG POST - created 3");
    }


    public String getExtraData()
    {
        LOG.info("BLOG POST - getting extra data");
        double rand = Math.random();
        return "This is some extra data on a blog post: " + rand ;
    }


}
