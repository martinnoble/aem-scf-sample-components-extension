package com.adobe.aem.scf.extensions;

import com.adobe.cq.social.SocialException;
import com.adobe.cq.social.commons.comments.api.AbstractCommentCollectionPrefetch;
import com.adobe.cq.social.commons.comments.api.PageInfo;
import com.adobe.cq.social.commons.comments.listing.CommentSocialComponentList;
import com.adobe.cq.social.commons.comments.listing.CommentSocialComponentListProvider;
import com.adobe.cq.social.commons.comments.listing.CommentSocialComponentListProviderManager;
import com.adobe.cq.social.forum.client.api.AbstractForum;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.CollectionPagination;
import com.adobe.cq.social.scf.JsonException;
import com.adobe.cq.social.scf.QueryRequestInfo;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

public class CustomBlogComponent extends AbstractForum {

    private static final Logger LOG = LoggerFactory.getLogger(CustomBlogComponent.class);

    private CommentSocialComponentList comments;
    private Boolean needComments = true;
    private QueryRequestInfo queryInfo;
    private PageInfo pageInfo;

    /**
     * Construct a comment for the specified resource and client utilities.
     * @param resource the specified resource
     * @param clientUtils the client utilities instance
     * @param commentListProviderManager list manager to use for listing content
     * @throws RepositoryException if an error occurs
     */
    public CustomBlogComponent(final Resource resource, final ClientUtilities clientUtils,
                               final CommentSocialComponentListProviderManager commentListProviderManager) throws RepositoryException{

        super(resource, clientUtils, commentListProviderManager);
        LOG.info("BLOG - created 1");

    }

    /**
     * Constructor of a comment.
     * @param resource the specified {@link com.adobe.cq.social.commons.Comment}
     * @param clientUtils the client utilities instance
     * @param queryInfo the query info.
     * @param commentListProviderManager list manager to use for listing content
     * @throws RepositoryException if an error occurs
     */
    public CustomBlogComponent(final Resource resource, final ClientUtilities clientUtils,
                               final QueryRequestInfo queryInfo, final CommentSocialComponentListProviderManager commentListProviderManager)
        throws RepositoryException
    {
        super(resource, clientUtils, queryInfo, commentListProviderManager);
        this.queryInfo = queryInfo;

        LOG.info("BLOG - created 2");

        LOG.info("Query string: " + queryInfo.getQueryString());

        List<String> resTypes = commentListProviderManager.getCommentSocialComponentListProvider(resource, queryInfo).getSupportedResourceType();
        for (String resType: resTypes)
        {
            LOG.info("Res type: " + resType);
        }

    }


    private void initComments() {
        LOG.info("Initialising comments");
        if(this.needComments) {
            this.needComments = false;
            QueryRequestInfo commentsQueryInfo = QueryRequestInfo.DEFAULT_QUERY_INFO_FACTORY.create(this.queryInfo);
            commentsQueryInfo.setSortOrder(this.getConfiguration().getSortOrder());
            List<Map.Entry<String, Boolean>> sortFields = commentsQueryInfo.getSortFields();
            if(sortFields == null || sortFields.isEmpty()) {
                commentsQueryInfo.setSortFields(this.getConfiguration().getSortFields());
            }

            CollectionPagination currentPagination = commentsQueryInfo.getPagination();
            int pageSize = 3; //this.configuration.getPageSize();
            if(this.queryInfo != null) {
                pageSize = this.queryInfo.getPagination() == CollectionPagination.DEFAULT_PAGINATION ? pageSize : currentPagination.getSignedSize();
            }

            commentsQueryInfo.setPagination(new CollectionPagination(currentPagination.getOffset(), pageSize, currentPagination.getEmbedLevel() + 1, currentPagination.getSortIndex(), pageSize));
            CommentSocialComponentListProvider listProvider = this.getCommentListProviderManager().getCommentSocialComponentListProvider(this.resource, commentsQueryInfo);
            if(listProvider == null) {
                throw new SocialException(String.format("Could not find a SCF list provider for %1$s whose resource type is: %2$s.", new Object[]{this.resource.getPath(), this.resource.getResourceType()}));
            } else {
                //if(TranslationSCFUtil.isSmartRenderingOn(this.resource, this.clientUtils)) {
                //    commentsQueryInfo.setTranslationRequest(true);
                //}

                LOG.info("Query String: " + commentsQueryInfo.getQueryString());
                LOG.info("Query Predicates: " + commentsQueryInfo.getPredicates());

                this.comments = listProvider.getCommentSocialComponentList(this, commentsQueryInfo, this.clientUtils);

                LOG.info("Got: " + this.comments.size() + " comments");
                if(!this.queryInfo.isQuery()) {
                    this.pageInfo = new PageInfo(this, this.clientUtils, commentsQueryInfo.getPagination());
                } else {
                    this.pageInfo = new PageInfo(this, this.clientUtils, commentsQueryInfo.getPagination(), this.queryInfo.getQueryString());
                }

            }
        }
    }

    @Override
    public Map<String, Object> getAsMap() {
        LOG.info("BLOG - Returning map");
        this.initCommentsWithPrefetch();
        return super.getAsMap();
    }

    @Override
    public String toJSONString(boolean tidy) throws JsonException {
        this.initCommentsWithPrefetch();
        return super.toJSONString(tidy);
    }

    private void initCommentsWithPrefetch() {
        this.initComments();
        if(this.comments instanceof AbstractCommentCollectionPrefetch) {
            ((AbstractCommentCollectionPrefetch)this.comments).prefetchForGetAsMap();
        }

    }


    
    public String getExtraData()
    {
        LOG.info("BLOG - getting extra data");
        double rand = Math.random();
        return "This is some extra random data: " + rand;
    }
    


}
