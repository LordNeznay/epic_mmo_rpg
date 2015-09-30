define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "game-view",

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