'use strict';

angular.module('aws', ['ngMessages', 'infinite-scroll'])
.controller('QueryCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.submit = function() {
        if ($scope.query) {
            $http({
                url: '/api/queries',
                method: 'PUT',
                data: { q: $scope.query, l: 5 }
            }).success(function (data) {
                $scope.status = { success: true, id: data.messageId };
            }).error(function () {
                $scope.status = { error: true };
            });
            $scope.query = null;
        }
    };
}])
.controller('SearchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.items = [];
    $scope.loading = false;
    $scope.search = function(query, limit, next) {
        $scope.loading = true;
        $scope.limit = limit;
        if (!next) {
            $scope.items = [];
        }
        $http({
            url: '/api/search',
            method: 'GET',
            params: { q: query, l: limit, n: next }
        }).success(function (data) {
            $scope.items = $scope.items.concat(data.items);
            $scope.loading = false;
            $scope.status = { };
            if (data.nextToken) {
                $scope.next = data.nextToken;
            } else {
                $scope.next = null;
            }
        }).error(function () {
            $scope.loading = false;
            $scope.status = { error: true };
        });
    };
}])
.controller('AsgCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.instances = [];
    $scope.loading = false;
    $scope.scaling = function(ip) {
        $scope.loading = true;
        if (!ip) {
            $http({
                url: '/api/scaling',
                method: 'GET'
            }).success(function (data) {
                $scope.instances = data;
                $scope.loading = false;
            }).error(function () {
                $scope.loading = false;
            });
        } else {
            $http({
                url: '/api/scaling',
                method: 'PUT',
                params: { ip: ip }
            }).success(function (data) {
                var idx = _.findIndex($scope.instances, function(instance) {
                    return instance.privateIp == data.privateIp;
                });
                if (idx >= 0) {
                    $scope.instances[idx] = data;
                } else {
                    $scope.instances.push(data);
                }
                $scope.loading = false;
            }).error(function () {
                $scope.loading = false;
            })
        }
    };
    $scope.scaling();
}])
.controller('LogsCtrl', ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
    $scope.logs = [];
    var retrieve = function() {
        $http({
            url: '/api/logs',
            method: 'GET'
        }).success(function (data) {
            var formatted = _.map(data, function(entry) {
                entry.logger = entry.logger.split('.').pop();
                return entry;
            });
            $scope.logs = _.sortBy($scope.logs.concat(formatted), ['timestamp', 'logger', 'message']).reverse().slice(0, 50);
            retrieve();
        }).error(function () {
            $timeout(retrieve, 5000);
        });
    };
    retrieve();
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
            
            var complete = function(fn, field) {
                scope.loading = false;
                scope.status = { };
                scope.status[field] = true;
                $timeout(fn, 5000);
            };

            var retrieve = function() {
                scope.loading = true;
                $http({
                    url: '/api/queues/' + scope.queue.QueueProperty,
                    method: 'GET'
                }).success(function (data) {
                    scope.queue.QueueName = data.attributes.QueueArn.split(':').pop();
                    _.assign(scope.queue, data.attributes);
                    complete(retrieve, 'success')
                }).error(function () {
                    complete(retrieve, 'error')
                });
            };
            
            retrieve();
        }
    }
}]);
