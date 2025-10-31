import React from 'react';

const Footer = () => {
    const customBgColor = {

        backgroundColor: '#335C67'
    };

    return (
        <footer className="text-white py-4 mt-auto" style={customBgColor}>
            <div className="container">
                <div className="row">

                    <div className="col-md-4 mb-3 text-center">
                        <h5>MetaMapa</h5>
                        <ul className="list-unstyled">
                            <li>
                                <a href="/acerca-de" className="text-white text-decoration-none opacity-75">
                                    Acerca de MetaMapa
                                </a>
                            </li>
                            <li>
                                <a href="/como-funciona" className="text-white text-decoration-none opacity-75">
                                    Cómo Funciona
                                </a>
                            </li>
                            <li>
                                <a href="/estadisticas" className="text-white text-decoration-none opacity-75">
                                    Estadísticas
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div className="col-md-4 mb-3 text-center">
                        <h5>Soporte</h5>
                        <ul className="list-unstyled">
                            <li>
                                <a href="/faq" className="text-white text-decoration-none opacity-75">
                                    Preguntas Frecuentes
                                </a>
                            </li>
                            <li>
                                <a href="/reportar-problema" className="text-white text-decoration-none opacity-75">
                                    Reportar un Problema
                                </a>
                            </li>
                            <li>
                                <a href="/contacto" className="text-white text-decoration-none opacity-75">
                                    Contacto
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div className="col-md-4 mb-3 text-center">
                        <h5>Legal</h5>
                        <ul className="list-unstyled">
                            <li>
                                <a href="/terminos" className="text-white text-decoration-none opacity-75">
                                    Términos y Condiciones
                                </a>
                            </li>
                            <li>
                                <a href="/privacidad" className="text-white text-decoration-none opacity-75">
                                    Política de Privacidad
                                </a>
                            </li>
                        </ul>
                    </div>

                </div>

                <hr className="opacity-100" />

                <div className="d-flex flex-column flex-sm-row justify-content-between align-items-center pt-3">
                    <p className="small mb-2 mb-sm-0">
                        &copy; {new Date().getFullYear()} MetaMapa. Todos los derechos reservados.
                    </p>

                </div>
            </div>
        </footer>
    );
};

export default Footer;