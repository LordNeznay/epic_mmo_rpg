define([
    'backbone',
    'tmpl/login'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "login-view",
        initialize: function () {
            this.render();
            this.hide();
        },
        render: function () {
            this.$el.html( this.template() );
        },
        show: function () {
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }

    });

    return new View();
});