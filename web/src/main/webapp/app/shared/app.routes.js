//module is called and configured
var app=angular.module('REALM');
app.config(function ($stateProvider, $urlRouterProvider) {


    app.stateProvider = $stateProvider;
    //Define URL/template/controller for each state
    $stateProvider
        //authentication
        .state('login', {url:'/login', templateUrl: 'shared/views/login/login.html', controller: 'LoginController', data: {authorizedRoles:['guest']}})
        .state('signup', {url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})

        //students
        .state('studentHome',{url: '/studentHome', templateUrl: 'shared/views/account/studentHome.html', controller: 'StudentHomeController', data: {authorizedRoles:['student']}})
        .state('studentProfile',{url: '/studentProfile', templateUrl: 'shared/views/account/studentProfile.html', controller: 'StudentProfileController', data: {authorizedRoles:['student']}})
        .state('studentCourses',{url: '/studentCourses', templateUrl: 'shared/views/account/studentCourses.html', controller: 'StudentCoursesController', data: {authorizedRoles:['student']}})
        .state('studentSessions',{url: '/studentSessions', templateUrl: 'shared/views/session/studentSessions.html', controller: 'StudentSessionsController', data: {authorizedRoles:['student']}})
        .state('notifications',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})

        //teachers
        .state('teacherHome',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})
        .state('teacherCourses',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})
        .state('teacherAssignments',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})
        .state('teacherSessions',{url: '/teacherSessions', templateUrl: 'shared/views/session/teacherSessions.html', controller: 'TeacherSessionsController', data: {authorizedRoles:['teacher']}})
        .state('reviewsSessions',{url: '/reviewSessions', templateUrl: 'shared/views/session/reviewSessions.html', controller: 'ReviewSessionsController', data: {authorizedRoles:['teacher']}})
        .state('teacherStudents',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})
        .state('teacherProfile',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})
        .state('adminHome',{url: '/signup', templateUrl: 'shared/views/login/signup.html', controller: 'SignupController', data: {authorizedRoles:['guest']}})

        //experiment state
        .state('experiment',{url: '/experiment', templateUrl: 'shared/views/experiment/experiment.html', controller: 'ExperimentController', data: {authorizedRoles:['student']}});
        /*states for specific experiments
        .state('experiment.template',{url: '/template', templateUrl: 'shared/views/experiment/experimentTemplate.html', controller: 'ExperimentTemplateController', data: {authorizedRoles:['student']}})
        .state('experiment.forwardKinematics',{url: '/forwardKinematics', templateUrl: 'mico/views/experiments/forwardkinematics.html', controller: 'ForwardKinematicsController', data: {authorizedRoles:['student']}})
        .state('experiment.inverseKinematics',{url: '/inverseKinematics', templateUrl: 'mico/views/experiments/inversekinematics.html', controller: 'InverseKinematicsController', data: {authorizedRoles:['student']}})
        .state('experiment.dynamicResponseAndControl',{url: '/dynamicResponseAndControl', templateUrl: 'mico/views/experiments/dynamicresponse.html', controller: 'DynamicResponseController', data: {authorizedRoles:['student']}})
        .state('experiment.teachPoints',{url: '/teachPoints', templateUrl: 'mico/views/experiments/teachpoints.html', controller: 'TeachPointsController', data: {authorizedRoles:['student']}})
        .state('experiment.dynamicResponse',{url: '/dynamicResponse', templateUrl: 'mico/views/experiments/teachpointsanddynamicresponse.html', controller: 'TeachPointsAndDynamicResponseController', data: {authorizedRoles:['student']}});*/
    //Redirect to login state if URL does not correspond to a defined state
    $urlRouterProvider
        .otherwise('/login');
});


app.run(function ($rootScope, AUTH_EVENTS, AuthService) {
    //check if session storage has any states stored!
    //if yes, then add the states
    //------------------------------------------------
    $rootScope.$on('$stateChangeStart', function (event, next) {
        var authorizedRoles = next.data.authorizedRoles;
        if(next.url == '/login' || next.url == '/signup' || next.url == '/studentHome'){
            //No need to authorize, anyone can access
        }
        else if(!AuthService.isAuthorized(authorizedRoles)) {
            event.preventDefault();
            if (AuthService.isAuthenticated()) {
                // user is not allowed
                $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
            } else {
                // user is not logged in
                $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
            }
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
   // localStorage.basePath="http://localhost:8080/realm/";
    localStorage.basePath=currentURL;
}
console.log("basePath is: "+localStorage.basePath);
