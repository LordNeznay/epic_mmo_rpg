define([
    'backbone',
    'tmpl/main',
    'views/base'
], function(
    Backbone,
    tmpl,
    BaseView
){

    var View = BaseView.extend({
        template: tmpl,
        className: "main-view",
		
        events: {
            "click a": "hide"
        },

    });

    return new View({content: '.page-main'});
});