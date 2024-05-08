import Home from '~/pages/Home';
import Following from '~/pages/Following';
import SignIn from '~/pages/SignIn';
import SignUp from '~/pages/SignUp';
// import DefaultLayout from '~/components/Layouts/DefaultLayout';

const publicRoutes = [
    { path: '/', component: Home },
    { path: '/following', component: Following },
    { path: '/sign-in', component: SignIn },
    { path: '/sign-up', component: SignUp },

];

const privateRoutes = [];

export { publicRoutes, privateRoutes };
