define([
    'backbone',
], function(
    Backbone
){

    var ViewManager = Backbone.View.extend({
        hideViews: function(view) {
            this.views.forEach(function(h_view, index, arr) {
                if (view.cid != h_view.cid) {
                    //console.log(h_view.name);
                    h_view.hide(); 
                }
            });
        },
        
        initialize: function(views) {
            this.views = views;
            var that = this;
            this.views.forEach(function(view, index, arr) {
                 that.listenTo(view, 'show', that.hideViews);
            });
        }
        
    });    

    return ViewManager;
});