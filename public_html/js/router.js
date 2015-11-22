define([
    'backbone',
	'views/main',	//����������� ���� � ����� �� ����� js
	'views/game',
	'views/scoreboard',
	'views/registration',
	'views/login',
    'views/manager'
], function(
    Backbone,
	mainView,	//���� ��������
	gameView,
	scoreboardView,
	registrationView,
	loginView,
    viewManager
){
	var views = [mainView, gameView, scoreboardView, registrationView, loginView];
        
    var ViewManager = new viewManager(views);

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
			'registration' : 'registrationAction',
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
		registrationAction: function(){
			registrationView.show();
		},
        loginAction: function () {
            loginView.show();
        }
    });

    return new Router();
});