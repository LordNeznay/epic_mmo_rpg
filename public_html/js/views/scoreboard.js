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