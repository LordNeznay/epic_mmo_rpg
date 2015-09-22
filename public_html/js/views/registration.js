define([
    'backbone',
    'tmpl/registration'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "login-registration",
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