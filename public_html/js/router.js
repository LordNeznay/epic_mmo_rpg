define([
    'backbone',
	'views/main',	//����������� ���� � ����� �� ����� js
	'views/game',
	'views/scoreboard',
	'views/login'
], function(
    Backbone,
	mainView,	//���� ��������
	gameView,
	scoreboardView,
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
            '*default': 'defaultActions'
        },
        defaultActions: function () {
            mainView.show();	//�������� �������
        },
        scoreboardAction: function () {
            scoreboardView.show();
        },
        gameAction: function () {
            gameView.show();
        },
        loginAction: function () {
            loginView.show();
        }
    });

    return new Router();
});