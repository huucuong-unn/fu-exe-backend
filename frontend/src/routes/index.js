import LayoutWithFooter from '~/components/Layouts/LayoutWithFooter';
import Dashboard from '~/pages/Dashboard';
import Following from '~/pages/Following';
import Home from '~/pages/Home';
import LandingPage from '~/pages/LandingPage';
import SignInSide from '~/pages/SignInSide';
import SignUp from '~/pages/SignUp';
// import DefaultLayout from '~/components/Layouts/DefaultLayout';

const publicRoutes = [
    { path: '/', component: Home },
    { path: '/following', component: Following },
    { path: '/sign-in', component: SignInSide, layout: null },
    { path: '/sign-up', component: SignUp },
    { path: '/dashboard', component: Dashboard },
    { path: '/page', component: LandingPage, layout: LayoutWithFooter },
];

const privateRoutes = [];

export { privateRoutes, publicRoutes };
