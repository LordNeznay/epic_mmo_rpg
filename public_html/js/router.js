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
            mainView.render();	//Вызываем функцию
        },
        scoreboardAction: function () {
            scoreboardView.render();
        },
        gameAction: function () {
            gameView.render();
        },
		registrationAction: function(){
			registrationView.render();
		},
        loginAction: function () {
            loginView.render();
        }
    });

    return new Router();
});