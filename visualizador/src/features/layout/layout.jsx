import { Outlet } from 'react-router-dom';
import NavBar from '../../components/nav-bar/nav-bar.jsx';
import Footer from '../../features/footer/footer.jsx';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/nav-bar/nav-bar.css';

function Layout() {
    return (
        <section className="app-layout">
            <NavBar />
            <main className="app-content">
                <Outlet/>
            </main>
            <Footer />
        </section>
    )
}

export default Layout;