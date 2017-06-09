/*
 * Copyright 2013 Adobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function($CQ, _, Backbone, SCF) {
    "use strict";

    console.log("registering blog post SCF component");

    var BlogPost = SCF.Topic.extend({
        modelName: "BlogPostModel",
        setExtraData: function(extraData) {
            var error = _.bind(function(jqxhr, text, error) {
                this.log.error("error setting extradata " + error);
                this.trigger('blog:extradataerror', {
                    'error': error
                });
            }, this);
            var success = _.bind(function(response) {
                this.set(response.response);
                this.trigger('blog:extradataset', {
                    model: this
                });
                this.trigger(this.events.UPDATED, {
                    model: this
                });
            }, this);
            var postData = {
                'extraData': extraData,
                ':operation': 'social:setBlogPostExtraData'
            };
            $CQ.ajax(SCF.config.urlRoot + this.get('id') + SCF.constants.URL_EXT, {
                dataType: 'json',
                type: 'POST',
                xhrFields: {
                    withCredentials: true
                },
                data: this.addEncoding(postData),
                'success': success,
                'error': error
            });
        }
    });

    var BlogPostView = SCF.TopicView.extend({
        viewName: "BlogPost",
        setExtraData: function(e){
            var extraData = this.getField("extraData");
            this.model.setExtraData(extraData);
            e.preventDefault();
        }
    });


    SCF.BlogPost = BlogPost;
    SCF.BlogPostView = BlogPostView;

    SCF.registerComponent('acme/components/blog/post', SCF.BlogPost, SCF.BlogPostView);
})($CQ, _, Backbone, SCF);