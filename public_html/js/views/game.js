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
        initialize: function () {
		
        },
        render: function () {

        },
        show: function () {
            $("#page").html( this.template() );
        },
        hide: function () {

        }

    });

    return new View();
});