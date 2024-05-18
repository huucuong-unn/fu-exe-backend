import Home from '~/pages/Home';
import Following from '~/pages/Following';
import SignIn from '~/pages/SignIn';
import SignUp from '~/pages/SignUp';
import { Dashboard } from '@mui/icons-material';
import Chart from '~/pages/Chart';
import Orders from '~/pages/Orders';
import Deposits from '~/pages/Deposits';
import { mainListItems } from '~/pages/listItems';
// import DefaultLayout from '~/components/Layouts/DefaultLayout';

const publicRoutes = [
    { path: '/', component: Home },
    { path: '/following', component: Following },
    { path: '/sign-in', component: SignIn },
    { path: '/sign-up', component: SignUp },
    { path: '/dashboard', component: Dashboard },
    { path: '/chart', component: Chart },
    { path: '/orders', component: Orders },
    { path: '/deposits', component: Deposits },
    { path: '/list-items', component: mainListItems},



];

const privateRoutes = [];

export { publicRoutes, privateRoutes };
