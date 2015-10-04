define([
    'backbone',
	'models/score'
], function(
    Backbone,
	PlayerModel
){

	var players = [
		new PlayerModel({name:'Dmitriy', score: 4783}),
		new PlayerModel({name:'Elena', score: 6438}),
		new PlayerModel({name:'Alexander', score: 2085}),
		new PlayerModel({name:'Yaroslav', score: 9475}),
		new PlayerModel({name:'Svyatoslav', score: 8046}),
		new PlayerModel({name:'Svetlana', score: 3150}),
		new PlayerModel({name:'Nicholas', score: 1193}),
		new PlayerModel({name:'Catherine', score: 9952}),
		new PlayerModel({name:'Evdokia', score: 1059}),
		new PlayerModel({name:'Gleb', score: 3026})
	];

    var Collection = Backbone.Collection.extend({
		model: PlayerModel,
		comparator: function (playerA, playerB) {
			var scoreDiff = playerB.get('score') - playerA.get('score');
			if (scoreDiff === 0) {
				 return playerA.get('name') < playerB.get('name') ? -1 : 1;
			}
			return scoreDiff;
		}
    });

    return new Collection(players);
});