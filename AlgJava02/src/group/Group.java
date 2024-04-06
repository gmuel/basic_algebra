package group;


/**
 * Dummy interface for the group implementation: two sub interfaces intended
 * <ol><li>{@link AbelGroup} an Abelian group (implementing {@link AbelGroup#add})</li>
 * <li>{@link NonAbelGroup} a non-Abelian group (implementing {@link NonAbelGroup#multiply})</li></ol>
 * Both methods are intended to override the {@link Monoid#operate(Monoid)} method
 * @author gmueller
 *
 */
public interface Group<G extends Group<G>> extends Monoid<G>{}
