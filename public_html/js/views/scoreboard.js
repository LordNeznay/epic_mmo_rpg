define([
    'backbone',
    'tmpl/scoreboard'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "scoreboard-view",
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