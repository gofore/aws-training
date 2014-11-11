'use strict';

angular.module('aws', ['ui.bootstrap'])
    .controller('View2Ctrl', ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {

        /*
        var tick = function() {
            $http.get('http://funchat-api.herokuapp.com/messages')
                .success(function (data) {
                    $scope.messages = data;
                });
            $timeout(tick, 100);
        };

        tick();
        */
    }])
    
.directive('sqs', ['$http', '$timeout', function($http, $timeout) {
    return {
        scope: { },
        templateUrl: 'partials/sqs.html',
        link: function(scope, element, attrs) {
            scope.loading = false;
            scope.queue = {
                QueueProperty: attrs.sqs
            };
            
            var complete = function(fn) {
                scope.loading = false;
                $timeout(fn, 30000);
            };

            var retrieve = function() {
                scope.loading = true;
                $http({
                    url: 'http://localhost:9001/api/queues/' + scope.queue.QueueProperty,
                    method: 'GET'
                }).success(function (data) {
                    scope.queue.QueueName = data.attributes.QueueArn.split(':').pop();
                    _.assign(scope.queue, data.attributes);
                    complete(retrieve)
                }).error(function () {
                    complete(retrieve)
                });
            };
            
            retrieve();
        }
    }
}]);
