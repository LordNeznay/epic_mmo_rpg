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
            $("#page").html( this.template() );
        },
        show: function () {
			alert("1");
        },
        hide: function () {

        }

    });

    return new View();
});