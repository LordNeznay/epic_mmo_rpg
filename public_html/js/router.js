define([
    'backbone',
	'views/main',	//Прописываем путь к файлу из папки js
	'views/game',
	'views/scoreboard',
	'views/registration',
	'views/login'
], function(
    Backbone,
	mainView,	//Даем название
	gameView,
	scoreboardView,
	registrationView,
	loginView
){
	$("#page").append(mainView.el);
	$("#page").append(gameView.el);
	$("#page").append(scoreboardView.el);
	$("#page").append(loginView.el);

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
			'registration' : 'registrationAction',
            '*default': 'defaultActions'
        },
        defaultActions: function () {
            mainView.show();	//Вызываем функцию
        },
        scoreboardAction: function () {
            scoreboardView.show();
        },
        gameAction: function () {
            gameView.show();
        },
		registrationAction: function(){
			registrationView.show();
		},
        loginAction: function () {
            loginView.show();
        }
    });

    return new Router();
});