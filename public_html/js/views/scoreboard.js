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
		
        },
        render: function () {
            $("#page").html( this.template() );
        },
        show: function () {

        },
        hide: function () {

        }

    });

    return new View();
});