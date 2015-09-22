define([
    'backbone',
	'views/main',	//����������� ���� � ����� �� ����� js
	'views/game',
	'views/scoreboard',
	'views/registration',
	'views/login'
], function(
    Backbone,
	mainView,	//���� ��������
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
            mainView.render();	//�������� �������
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