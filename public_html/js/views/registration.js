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

		events: {
            "submit .signup-form__form": "submitSignup",
            "click a": "hide"
        },
        render: function () {
			this.$el.html( this.template() );
        },
        show: function () {
            this.render();
			//≈сли пользователь авторизован, то перенаправить на логин
			$.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
				dataType: 'json',
                success: function(data){	
					if(data.isLogin == 'true'){
						Backbone.history.navigate('login', true);
					}
                }
            });
        },
        hide: function () {
			this.$el.empty();
        },
		submitSignup: function(){
			var pView = this;	
			var values = $('.signup-form__form').serialize();
			$.ajax({
				type: "POST",
				data: values,
                url: "/api/v1/auth/signup",
				dataType: 'json',
                success: function(data) {
					if(data.errors == 'null'){
						//ќшибок нет, значит авторизуем пользовател€
						$.post("/api/v1/auth/signin", values);
						Backbone.history.navigate('login', true);
					} else {
						$(".signup-form__errors").html(data.errors);
					}
                }
            });
			return false;
		}

    });

    return new View({el: $('.page')});
});