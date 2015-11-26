define([
    'backbone',
    'models/score',
    'sync/highscoresSync'
], function(
    Backbone,
    PlayerModel,
    highscoresSync
){

    /*var players = [
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
    ];*/

    var Collection = Backbone.Collection.extend({
        model: PlayerModel,
        comparator: function (playerA, playerB) {
            var scoreDiff = playerB.get('score') - playerA.get('score');
            if (scoreDiff === 0) {
                return playerA.get('name') < playerB.get('name') ? -1 : 1;
            }
            return scoreDiff;
        },
        
        parse: function(data){
            var that = this;
            //data.forEach(function(one_player){
            //    that.add(one_player);
            //});
            that.reset(data);
            that.trigger('loadScoreboard', that.toJSON());
        },
        sync: highscoresSync
    });

    var collection = new Collection();
    //collection.fetch();
    
    return collection;
});