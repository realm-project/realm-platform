//module is called and configured
var app=angular.module('REALM');
app.config(function ($stateProvider, $urlRouterProvider) {


    app.stateProvider = $stateProvider;
    //Define URL/template/controller for each state
    $stateProvider
        //authentication
        .state('login', {url:'/login', templateUrl: 'shared/views/login/login.html', controller: 'LoginController', data: {authorizedRoles:['guest']}})
        .state('signup', {url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})

        .state('resetPass', {url:'/resetPass/:tokenID', templateUrl: 'shared/views/login/resetPass.html', controller: 'ResetPassController', data: {authorizedRoles:['guest']}})

        //students
        .state('studentHome',{url: '/studentHome', templateUrl: 'shared/views/account/studentHome.html', controller: 'StudentHomeController', data: {authorizedRoles:['student','teacher'],pageName:"Home"}})
        .state('studentHome.profile',{url: '/profile', templateUrl: 'shared/views/account/studentProfile.html', controller: 'StudentProfileController', data: {authorizedRoles:['student','teacher'],pageName:"Profile"}})
        .state('studentHome.courses',{url: '/courses', templateUrl: 'shared/views/account/studentCourses.html', controller: 'StudentCoursesController', data: {authorizedRoles:['student','teacher'],pageName:"Courses"}})
        .state('studentHome.sessions',{url: '/sessions', templateUrl: 'shared/views/session/studentSessions.html', controller: 'StudentSessionsController', data: {authorizedRoles:['student','teacher'],pageName:"Sessions"}})
        //.state('notifications',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})

        //teachers
        .state('teacherHome',{url: '/teacherHome', templateUrl: 'shared/views/account/teacherHome.html', controller: 'teacherHomeController', data: {authorizedRoles:['teacher'],pageName:"Home"}})
        .state('teacherHome.profile',{url: '/profile', templateUrl: 'shared/views/account/studentProfile.html', controller: 'StudentProfileController', data: {authorizedRoles:['teacher'],pageName:"Profile"}})
        .state('teacherHome.courses',{url: '/courses', templateUrl: 'shared/views/account/studentCourses.html', controller: 'StudentCoursesController', data: {authorizedRoles:['teacher'],pageName:"Courses"}})
        .state('teacherHome.mySessions',{url: '/mySessions', templateUrl: 'shared/views/session/studentSessions.html', controller: 'TeacherMySessionsController', data: {authorizedRoles:['teacher'],pageName:"Sessions"}})
        .state('teacherHome.createSessions',{url: '/createSessions', templateUrl: 'shared/views/session/teacherSessions.html', controller: 'TeacherSessionsController', data: {authorizedRoles:['teacher'],pageName:"Create Sessions"}})
        .state('teacherHome.reviewsSessions',{url: '/reviewSessions', templateUrl: 'shared/views/session/reviewSessions.html', controller: 'ReviewSessionsController', data: {authorizedRoles:['teacher'],pageName:"Review Sessions"}});
        
    //Redirect to login state if URL does not correspond to a defined state
    $urlRouterProvider
        .otherwise('/login');
});


app.run(function ($rootScope, $state, AUTH_EVENTS, AuthService) {
    
    $rootScope.$on('$stateChangeStart', function (event, next) {
        var authorizedRoles = next.data.authorizedRoles;

        for (var index in authorizedRoles){
            if(authorizedRoles[index]=='guest'){
                //No need to authorize, anyone can access
            return;
            }
        }
            
        if(!AuthService.isAuthorized(authorizedRoles)) {
            event.preventDefault();
            if (AuthService.isAuthenticated()) {
                // user is not allowed
                $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
            } else {
                // user is not logged in
                $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
            }
            $state.go('login');
        }
    });
});

app.constant('AUTH_EVENTS', {
    loginSuccess: 'auth-login-success',
    loginFailed: 'auth-login-failed',
    logoutSuccess: 'auth-logout-success',
    sessionTimeout: 'auth-session-timeout',
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized',
    notFound: 'auth-not-found'
});

app.constant('GAMEPAD_EVENTS', {
    gamepadConnected: 'gamepad-connected',
    gamepadDisconnected: 'gamepad-disconnected'

});

app.constant('USER_ROLES', {
    admin: 'admin',
    teacher: 'teacher',
    student: 'student',
    guest: 'guest'
});

//Take the current URL
var currentURL = window.location.href;

//...search for index to splice on
var indexOfSpecificPath = currentURL.indexOf('app');

//splice away4
localStorage.basePath = currentURL.slice(0,indexOfSpecificPath);
//alert('BASE PATH = ' + window.basePath);


if(document.location.hostname==="localhost")
{
    var indexOfSpecificPath = currentURL.indexOf('app');
    localStorage.basePath = currentURL.slice(0,indexOfSpecificPath);
}
