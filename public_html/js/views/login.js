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

		events: {
            "submit .login-form__form": "submitLogin",
			"submit .unlogin-form__form": "submitUnlogin",
            "click a": "hide"
        },
        render: function () {
			this.$el.html( this.template() );
        },
        show: function () {
            this.render();
			$.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
				dataType: 'json',
                success: function(data){	
					//В зависимости от статуса пользователя выбираем форму
					if(data.isLogin == 'true'){
						$(".login-form").hide();
						$(".unlogin-form").show();
					} else {
						$(".unlogin-form").hide();
						$(".login-form").show();
					}
                }
            });
        },
        hide: function () {
			this.$el.empty();
        },
		submitLogin: function(event){
			var pView = this;	
			$.ajax({
				type: "POST",
				data: $('.login-form__form').serialize(),
                url: "/api/v1/auth/signin",
				dataType: 'json',
                success: function(data) {
					if(data.errors == 'null'){
						pView.show();
					} else {
						//Выводим ошибки
						$(".login-form__errors").html(data.errors);
					}
                }
            });
			return false;
		},
		submitUnlogin: function(event){
			var pView = this;
            $.ajax({
                type: "POST",
                url: "/api/v1/auth/exit",
                success: function(data){
                   pView.show();
				}
            });
            return false;
		}

    });

    return new View({el: $('.page')});
});