package awtzero;


import java.awt.event.KeyEvent;

/**
 * A large enum containing all the keys that can be used with AWTZero's Keyboard class.
 * @see awtzero.Keyboard
 * Example usage: {@code keyboard.isKeyDown(Key.LEFT);}
 */
public enum Key {
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),

    SPACE(KeyEvent.VK_SPACE),
    BACKSPACE(KeyEvent.VK_BACK_SPACE),
    TAB(KeyEvent.VK_TAB),

    ESCAPE(KeyEvent.VK_ESCAPE),
    FUNCTION1(KeyEvent.VK_F1),
    FUNCTION2(KeyEvent.VK_F2),
    FUNCTION3(KeyEvent.VK_F3),
    FUNCTION4(KeyEvent.VK_F4),
    FUNCTION5(KeyEvent.VK_F5),
    FUNCTION6(KeyEvent.VK_F6),
    FUNCTION7(KeyEvent.VK_F7),
    FUNCTION8(KeyEvent.VK_F8),
    FUNCTION9(KeyEvent.VK_F9),
    FUNCTION10(KeyEvent.VK_F10),
    FUNCTION11(KeyEvent.VK_F11),
    FUNCTION12(KeyEvent.VK_F12),
    PRINTSCREEN(KeyEvent.VK_PRINTSCREEN),
    DELETE(KeyEvent.VK_DELETE),
    INSERT(KeyEvent.VK_INSERT),
    ASTERISK(KeyEvent.VK_ASTERISK),
    FORWARDSLASH(KeyEvent.VK_SLASH),
    BACKTICK(KeyEvent.VK_BACK_QUOTE),
    EXCLAIMATION(KeyEvent.VK_EXCLAMATION_MARK),
    
    N1(KeyEvent.VK_1),
    N2(KeyEvent.VK_2),
    N3(KeyEvent.VK_3),
    N4(KeyEvent.VK_4),
    N5(KeyEvent.VK_5),
    N6(KeyEvent.VK_6),
    N7(KeyEvent.VK_7),
    N8(KeyEvent.VK_8),
    N9(KeyEvent.VK_9),
    N0(KeyEvent.VK_0),
    
    A(KeyEvent.VK_A),
    B(KeyEvent.VK_B),
    C(KeyEvent.VK_C),
    D(KeyEvent.VK_D),
    E(KeyEvent.VK_E),
    F(KeyEvent.VK_F),
    G(KeyEvent.VK_G),
    H(KeyEvent.VK_H),
    I(KeyEvent.VK_I),
    J(KeyEvent.VK_J),
    K(KeyEvent.VK_K),
    L(KeyEvent.VK_L),
    M(KeyEvent.VK_M),
    N(KeyEvent.VK_N),
    O(KeyEvent.VK_O),
    P(KeyEvent.VK_P),
    Q(KeyEvent.VK_Q),
    R(KeyEvent.VK_R),
    S(KeyEvent.VK_S),
    T(KeyEvent.VK_T),
    U(KeyEvent.VK_U),
    V(KeyEvent.VK_V),
    W(KeyEvent.VK_W),
    X(KeyEvent.VK_X),
    Y(KeyEvent.VK_Y),
    Z(KeyEvent.VK_Z),
    
    SHIFT(KeyEvent.VK_SHIFT),
    ENTER(KeyEvent.VK_ENTER),
    CTRL(KeyEvent.VK_CONTROL),
    ALT(KeyEvent.VK_ALT)

    ;
	
    public final int code;

    Key(int code) {
        this.code = code;
    }
}
