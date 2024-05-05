import Home from '~/pages/Home';
import Following from '~/pages/Following';
import DefaultLayout from '~/components/Layouts/DefaultLayout';

const publicRoutes = [
    { path: '/', component: Home },
    { path: '/following', component: Following },
];

const privateRoutes = [];

export { publicRoutes, privateRoutes };
