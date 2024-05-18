import Header from '~/Parts/Header';
import Footer from '~/Parts/Footer';
import classNames from 'classnames/bind';
import styles from '~/components/Layouts/DefaultLayout/DefaultLayout.module.scss';

const cx = classNames.bind(styles);

function DefaultLayout({ children }) {
    return (
        <div className={cx('page-container')}>
            <Header />

            <div className={cx('content-container')}>
                <div className={cx('content')}>{children}</div>
            </div>
            <Footer />
        </div>
    );
}

export default DefaultLayout;
