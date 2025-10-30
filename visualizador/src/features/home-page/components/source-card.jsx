function SourceCard({ text, alignRight, scrollOffset, delay, icon }) {
    const startPos = 150;

    let translateXDistance = startPos - scrollOffset;

    if (translateXDistance < 0) {
        translateXDistance = 0;
    }

    const finalTransform = alignRight
        ? `translateX(-${translateXDistance}px)`
        : `translateX(${translateXDistance}px)`;


    const transitionStyle = {
        transform: finalTransform,
        opacity: 1 - translateXDistance / startPos,
        transition: `opacity 0.1s ease-out, transform 0.1s ease-out`,
        backgroundColor: '#e69553',
        borderRadius: '50px',
        padding: '20px 40px',
        margin: '20px 0',
        color: 'white',
        fontWeight: '500',
        minHeight: '120px',
        display: 'flex',
        alignItems: 'center',
        gap: '30px',
        flexDirection: alignRight ? 'row-reverse' : 'row',
    };

    const iconWrapperStyle = {
        backgroundColor: 'transparent', // El ícono ya tiene color
        borderRadius: '50%',
        width: '80px',
        height: '80px',
        flexShrink: 0,
        // Centrar el ícono dentro del wrapper
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    };

    return (
        <div
            style={{ ...transitionStyle, transitionDelay: `${delay}s` }}
            className="col-12 col-md-8 mx-auto"
        >
            <div className="circle-background-style">
                {icon}
            </div>
            <p className="mb-0 text-start">{text}</p>
        </div>
    );
};

export default SourceCard;