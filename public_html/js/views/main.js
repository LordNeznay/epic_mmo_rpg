define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        template: tmpl,
        className: "main-view",
		
        events: {
            "click a": "hide"
        },
        render: function () {
            this.$el.html( this.template() );
        },
        show: function () {
            this.render();
        },
        hide: function () {
            this.$el.empty();
        }

    });

    return new View({el: $('.page')});
});