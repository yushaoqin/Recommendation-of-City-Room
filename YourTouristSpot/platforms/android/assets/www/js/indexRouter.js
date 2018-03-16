var app = angular.module('ionicApp', ['ionic','angularFileUpload']);

app.config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('tabs', {
                url: "/tab",
                abstract: true,
                templateUrl: "templates/tabs.html"
            })
            .state('tabs.home', {
                url: "/home",
                views: {
                    'home-tab': {
                        templateUrl: "templates/home.html",
                        controller: 'HomeTabCtrl'
                    }
                }
            })
            .state('tabs.search_result', {
                url: "/search",
                views: {
                    'home-tab': {
                        templateUrl: "templates/searchResult.html",
                        controller: 'SearchResultCtrl'
                    }
                }
            })
            .state('tabs.personal', {
                url: "/personal",
                views: {
                    'personal-tab': {
                        templateUrl: "templates/personal.html",
                        controller: 'PersonalTabCtrl'
                    }
                }
            })
            .state('tabs.login', {
                url: "/login",
                views: {
                    'personal-tab': {
                        templateUrl: "templates/logIn.html",
                        controller: 'PersonalTabCtrl'
                    }
                }
            })
            .state('tabs.recommendation', {
                url: "/recommendation",
                views: {
                    'contact-tab': {
                        templateUrl: "templates/recommendation.html",
                        controller:'RecommendationTabCtrl'
                    }
                }
            })
            .state('tabs.homeRoomInfo', {
                url: "/roomInfo?roomId/:id",
                views: {
                    'home-tab': {
                        templateUrl: "templates/room.html",
                        controller: 'RoomInfoCtrl'
                    }
                }
            })
            .state('tabs.roomInfo', {
                url: "/roomInfo?roomId/:id",
                views: {
                    'contact-tab': {
                        templateUrl: "templates/room.html",
                        controller: 'RoomInfoCtrl'
                    }
                }
            })
            .state('tabs.personalRoomInfo', {
                url: "/roomInfo?roomId/:id",
                views: {
                    'personal-tab': {
                        templateUrl: "templates/room.html",
                        controller: 'RoomInfoCtrl'
                    }
                }
            })

        ;


        $urlRouterProvider.otherwise("/tab/home");

    });

