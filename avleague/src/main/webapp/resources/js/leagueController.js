var avlModule = angular.module('avl', []);

avlModule.controller('LeagueController', function($scope, LeagueService) {
    LeagueService().then(function(response) {
        $scope.leagues = response.data;
    });
});

avlModule.factory('LeagueService', function($http) {
   return function() {
        return $http.get('/avl/api/leagues/').success(function(response) {
            console.log(response);
            return response;
        });
   }
});