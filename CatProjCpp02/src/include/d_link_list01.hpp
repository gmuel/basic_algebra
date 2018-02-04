/**
 * @file d_link_list.h
 *
 * Provides Hashing utilities, some useful error classes
 * and doubly linked list utilities. For brevity,
 * three macros provide limited access to this headers
 * functionalities:
 * <ol>
 * <li><code>D_LINK_LIST_H01_HASH_UTIL</code>: provides interface for hashing</li>
 * <li><code>D_LINK_LIST_H01_SIZED_D_LINKS</code> provides interface for size-aware
 * doubly linked lists (different sizes indicate different classes!)</li>
 * <li><code>D_LINK_LIST_H01_UNSIZED_D_LINKS</code> provides interface for size-indifferent
 * doubly linked lists (different sizes are simply different objects of the same class!)</li>
 * </ol>
 * Usage: prior to <code>#include "d_link_exp.h"</code> simply include:
 * <pre>#ifndef D_LINK_LIST_H01_UNSIZED_LINK
 * #define D_LINK_LIST_H01_UNSIZED_LINK
 * #endif
 * #include "d_link_list.h" //macro defined prior to imports
 * ...//now, we may use util::d_linked_exp<X > etc.</pre>
 *
 *  Created on: 07.08.2014
 *      Author: adin
 */

#ifndef D_LINK_LIST_H01_
#define D_LINK_LIST_H01_
#include <stdexcept>

/**
 * @brief Hash utilities

 *
 * Use D_LINK_LIST_H01_HASH_UTIL macro to get the
 * two classes <code>util::hashable</code> and <code>util::hash_obj</code>
 * The first type defines hashing functions for arbitrary types, the second

 * one acts as smart pointer for hashable objects
 */
#ifdef D_LINK_LIST_H01_HASH_UTIL
#include "hashable.hpp"
#endif

/**
 * The utility namespace
 */
namespace util {
/**
 * @brief some handy name
 */
typedef unsigned int u_int;

/**
 *
 * @brief An error class indicating some allocation process failed
 */
class alloc_error : public std::runtime_error {
public:
	/**
	 * Constructs an allocation error
	 * instance with <code>msg</code> string
	 */
	alloc_error(const std::string& msg):
		std::runtime_error(msg){}
	/**
	 * @brief Destructor
	 */
	virtual ~alloc_error()throw(){}
};
/**
 * @brief Concurrent modification exception class
 *
 * Thrown, whenever an object was altered without typical
 * synchronization techniques.
 * <p>For instance, <code>util::d_linked_exp_list</code> class
 * template allows multiple instantiations of its mutable
 * <code>util::d_linked_exp_list<X>::iterator</code>s within the same
 * scope which may cause unpredictable behavior, if two iterators are
 * pointing to the same entry and one tries to remove it the other is
 * left with a dangling pointer. Currently, nothing happens (the call is
 * silently ignored - to specify: returns) but this may change in the near
 * future (i.e. <p><code>void util::d_linked_exp_list<X >::iterator::erase() throw (concurr_except);</code>
 * would be the prototype of the removal member function)
 */
class concurr_except : public std::runtime_error {
public:
	/**
	 * @brief Message constructor
	 * @param msg message
	 */
	concurr_except(const std::string& msg):
		std::runtime_error(msg){
	}
	/**
	 * @brief Destructor
	 */
	~concurr_except()throw(){}
};
#ifdef D_LINK_LIST_H01_SIZED_D_LINKS
#include "d_list_sized.h"
#endif

/*
 * Use D_LINK_LIST_H01_UNSIZED_D_LINKS macro to get the
 * two classes <code>util::d_link_exp</code> and <code>util::d_linked_exp_list</code>
 * The first type defines some primitive smart pointer for arbitrary types, the second
 * one acts as list of those smart pointer
 */
#ifdef D_LINK_LIST_H01_UNSIZED_D_LINKS
template<class X >
class d_linked_exp_list;
/**
 * @brief The expandable doubly linked link class template
 * <p>Equivalent to <code>d_link<X,N></code>, only with
 * <code>N</code> unspecified to facilitate runtime insertions/
 * deletions.
 * @tparam X the type to get linked, <b>MUST</b> implement default,
 * 		copy constructor and assignment operator
 */
template<class X >
class d_link_exp {
	/**
	 * Addition member
	 * Adds all entries of entry_list
	 * to this
	 * @param entry_list
	 * @return a pointer to the head entry, i.e. with no
	 * 		predecessors
	 */
	d_link_exp<X >* add(const d_link_exp<X >& entry_list){
		const d_link_exp<X >* ptr1 = &entry_list;
		d_link_exp<X >* ptr2 = 0;
		while(ptr1->prev) ptr1 = ptr1->prev;
		while(ptr1){
			if(ptr2) ptr2 = ptr2->add(*ptr1->ptr,ptr1->index);
			else ptr2 = add(*ptr1->ptr,ptr1->index);
			ptr1 = ptr1->next;
		}
		while(ptr1->prev) ptr1 = ptr1->prev;
		return const_cast<d_link_exp<X >* >(ptr1);
	}
	/**
	 * Addition member
	 * Adds x to this list at list entry <code>l[index]</code>
	 * @param x the list entry
	 * @param indx the index
	 * @return a pointer of the new list entry
	 */
	d_link_exp<X >* add(const X& x, u_int indx){
		if(!ptr) {
			ptr = new X(x);
			if(index!=indx) index = indx;
			return this;
		}
		if(index<indx) {
			if(next&&next->index<indx) return next->add(x,indx);
			if(next&&next->index>=indx){
				if(next->index==indx) {
					*(next->ptr) = x;
					return next;
				}

				d_link_exp<X >* l_ptr = new d_link_exp<X >;
				l_ptr->ptr = new X(x);
				l_ptr->index = indx;
				l_ptr->next = next;
				next->prev = l_ptr;
				l_ptr->prev = this;
				next = l_ptr;
				++(*length);
				l_ptr->length = length;
				return l_ptr;
			}

			next = new d_link_exp<X >;
			next->ptr = new X(x);
			next->index = indx;
			++(*length);
			next->length = length;
			next->prev = this;
			return next;
		}
		if(index>indx){
			if(prev&&prev->index>indx) return prev->add(x,indx);
			if(prev&&prev->index<=indx){
				if(prev&&prev->index==indx){
					*(prev->ptr) = x;
					return prev;
				}

				d_link_exp<X >* l_ptr = new d_link_exp<X >;
				l_ptr->ptr = new X(x);
				l_ptr->index = indx;
				l_ptr->prev = prev;
				prev->next  = l_ptr;
				l_ptr->next = this;
				prev = l_ptr;
				++(*length);
				l_ptr->length = length;
				return l_ptr;

			}

			prev = new d_link_exp<X >;
			prev->ptr = new X(x);
			prev->index = indx;
			prev->next = this;
			++(*length);
			prev->length = length;

			return prev;
		}
		*ptr = x;
		return this;
	}
protected:
	/**@brief The pointer to the
	 * list entry*/
	X* ptr;
	/**@brief The pointer to its
	 * predecessor*/
	d_link_exp<X >* prev,
		/**@brief The pointer to its
	 	 * successor*/
		* next;
	/**@brief The length pointer*/
	u_int* length;
	/**@brief The index*/
	u_int index;
	/**
	 * @brief Default constructor
	 */
	d_link_exp():
		ptr(0),prev(0),
		next(0),length(0),
		index(0){}
	/**
	 * @brief Single entry constructor
	 *
	 * Constructs the list entry
	 * <code>l[indx] = x</code>
	 * @param x the list entry
	 * @param indx the index
	 */
	d_link_exp(const X& x, u_int indx = 0u):
		ptr(0),prev(0),
		next(0),length(0),
		index(indx)
	{
		ptr = new X(x);
		length = new u_int(1u);
	}
	/**
	 * @brief Copy constructor
	 * @param o the original
	 */
	d_link_exp(const d_link_exp<X >& o):
		ptr(0),prev(0),
		next(0),length(0),
		index(o.index)
	{
		add(o);
	}
	/**
	 * @brief List copy constructor
	 *
	 * Constructs a copy of list entries
	 * all present in list
	 * @param list to copy
	 */
	d_link_exp(const d_linked_exp_list<X >& list):
		ptr(0),prev(0),
		next(0),length(0),
		index(0)
	{

	}
	friend class d_linked_exp_list<X >;
	friend class const_iterator;
	friend class iterator;
public:
	/**
	 * @brief Destructor
	 */
	virtual ~d_link_exp(){
		if(next){
			delete next;
			next = 0;
		}
		if(ptr){
			delete ptr;
			ptr = 0;
		}
		if(length&&!prev){
			delete length;
			length = 0;
		}
		else {
			length = 0;
			//if(prev
			prev = 0;
		}
	}
	/**@brief The default value of type <code>X</code>
	 * <p>Used as dummy for dereference operator
	 * <br/><b>Note</b> this requires default constructor*/
	static const X& DEFAULT_VAL;

	/**
	 * @brief Index member
	 * @return the index of this list entry
	 */
	u_int idx() const {return index;}
	/**
	 * @brief Length member
	 * @return the length of this list entries
	 */
	u_int len() const {return length?*length:0;}
	/**
	 * @brief Dereference operator
	 * @return the reference of this list's entry or
	 * 		<code>DEFAULT_VAL</code>
	 */

	const X& operator*() const {
		if( ptr) return *ptr;
		return DEFAULT_VAL;
	}
	/**
	 * @brief Assignment operator
	 * @param x the new list entry
	 * @return the altered list entry
	 */
	d_link_exp<X >& operator=(const X& x){
		if(ptr) *ptr = x;
		else ptr = new X(x);
		return *this;
	}
	/**
	 * @brief Assignment operator
	 *
	 * Note, null arguments as <code>DEFAULT_VAL</code>
	 * leaves entry unchanged (in particular, no removal!). Furthermore,
	 * this member checks for index agreement and changes an entry accordingly.
	 * If no entry is found, it inserts a new link such that
	 * <code>prev->index < link.idx() < next->index</code> is true
	 * @param link the list entries to copy
	 * @return the altered list entry
	 */
	d_link_exp<X >& operator=(const d_link_exp<X >& link){
		/*
		const d_link_exp<X >* ptr2 = &link;
		while(ptr1->prev) ptr1 = ptr1->prev;
		while(ptr2->prev) ptr2 = ptr2->prev;
		while(ptr1&&ptr2){
			*ptr1->ptr = *ptr2->ptr;
			ptr1->index = ptr2->index;
			ptr1 = ptr1->next;
			ptr2 = ptr2->next;
		}
		if(ptr1&&!ptr2){
			delete ptr1;
			ptr1 = 0;
		}
		else if(!ptr1&&ptr2){
			ptr1 = this;
			while(ptr1->next) ptr1 = ptr1->next;
			while(ptr2){
				ptr1->next = new d_link_exp<X >;
				ptr1->next->ptr = new X(*ptr);
				ptr1->next->prev = ptr1;
				ptr1->next->length = length;
				ptr1 = ptr1->next;
				ptr2 = ptr2->next;
			}
		}
		*length = *link.length;
		*/
		if(!link.ptr) return *this;
		if(this!=&link){
			//d_link_exp<X >* ptr1 = this;
			if(index==link.index){
				return this->operator=(*link.ptr);
			}
			if(index>link.index){
				if(prev){
					if(prev->index<link.index){
						d_link_exp<X >* ptr1 = new d_link_exp<X >;
						ptr1->prev = prev;
						ptr1->next = this;
						prev->next = ptr1;
						prev = ptr1;
						ptr1->index = link.index;
						ptr1->ptr = new X(*link);
						ptr1->length = length;
						++(*length);
						return *this;
					}
					else {
						return prev->operator =(link);
					}
				}
				else if(!prev&&index>link.index){
					prev = new d_link_exp<X >;
					prev->next = this;
					prev->length = length;
					prev->ptr = new X(*link.ptr);
					++(*length);
					return *this;
				}
			}
			else {
				if(next)  {
					return next->operator =(link);
				}
				next = new d_link_exp<X >;
				next->prev = this;
				next->length = length;
				++(*length);
				next->ptr = new X(*link.ptr);
			}
		}
		return *this;
	}
	/**
	 * @brief Equality (static member)
	 * @param i1 first link instance
	 * @param i2 second link instance
	 * @return true if both equal (index and entry)
	 */
	friend bool operator==(const d_link_exp<X >& i1, const d_link_exp<X >& i2){
		return i1.index==i2.index&&*i1.ptr==*i2.ptr;
	}
	/**
	 * @brief Inequality member
	 * @param i1 first link
	 * @param i2 second link
	 * @return true if and only if <code>i1==i2</code> returns false
	 */
	friend bool operator!=(const d_link_exp<X >& i1, const d_link_exp<X >& i2){
		return !(i1==i2);
	}
};
/*
 * Class member instantiation
 */
template<class X >
const X& d_link_exp<X >::DEFAULT_VAL = X();

/**
 * @brief The doubly linked expandable list template
 *
 * Roughly equivalent to <code>d_linked_list</code> only
 * with the size parameter adjustable. Note, this class provides
 * primitive concurrent modification protection:
 * <p>an <code>d_linked_exp_list<X>::iterator</code> or <code>d_linked_exp_list<X>::const_iterator</code>
 * still attached to an list instance prevents its removal, by default with an exception.
 * Furthermore, this class provides subscripting operation
 * @param X the type of the list entries, <b>HAS TO</b> provide
 * 		default, copy constructor and assignment operator
 * 		or linkage error will occur
 */
template<class X >
class d_linked_exp_list {
public:
	class const_iterator;
	class iterator;
protected:
	typedef d_linked_exp_list<X> _list;
	/**An array view of the list*/
	d_link_exp<X >** entry_array;
	/**The list's head entry pointer*/
	d_link_exp<X >* first,
		/**The list's tail entry pointer*/
		* last;
	/**A pointer to a  constant bidirectional iterator instance*/
	mutable const const_iterator* has_const_iterator;
	/**A pointer to a mutable bidirectional iterator instance*/
	mutable iterator* has_muta_iterator;
	/**The size pointer*/
	volatile u_int size;
	/**
	 * Auxiliary member
	 * Sets all array view pointer to zero
	 * and deletes the array
	 */
	void del_array(){
		if(!entry_array) return;
		for (u_int i = 0; i < size; i++) entry_array[i] = 0;
		delete[] entry_array;
		entry_array = 0;
	}
	/**
	 * Auxiliary member
	 * Initializes the array of this list's
	 * array view only if size pointer is set
	 * and the array hasn't been initialized, yet
	 */
	void init_array(){
		if(!size) return;
		if(entry_array) return;
		entry_array = new d_link_exp<X >*[size];
		for(u_int i = 0; i < size; i++) entry_array[i] = 0;
	}
	/**
	 * Auxiliary member
	 * Inserts <code>x</code> at list entry
	 * <code>l[index]</code>
	 * @param index of list entry
	 * @param x value of list entry
	 */
	void insert(u_int index, const X& x){

#if _D_LINK_LIST_H01_NO_ITER_INSERT
		if(entry_array){
			if(size<=index) {
				del_array();
				size = index+1;
				init_array();
				set_array();
			}
			d_link_exp<X >* ptr = entry_array[index],* ptr0 = 0;
			if(ptr){
				*ptr = x;
			}
			else{
				if(index<last->index&&first->index<index){
					u_int i(0);
					bool lower (index-i>=0), upper(index+i<size);
					while(!(ptr||ptr0)&&(lower||upper)){
						if(lower) ptr = entry_array[index-i];
						if(upper) ptr0= entry_array[index+1];
						++i;
						lower = index-i>=0;
						upper = index+i<size;
					}
					if(ptr&&!ptr0){
						ptr0 = ptr->add(x,index);
						entry_array[index] = ptr0;
					}
					else {
						ptr = ptr0->add(x,index);
						entry_array[index] = ptr;
					}
				}
				else if(index<first->index) {
					entry_array[index] = first->add(x,index);
					first = entry_array[index];
				}
				else {
					entry_array[index] = last->add(x,index);
					last = entry_array[index];
					size = last->index+1;
				}
			}
			return;
		}
		if(first) {
			d_link_exp<X >* ptr = first->add(x,index);
			if(ptr==first) {
				last = ptr;
				del_array();
				size = last->index+1;
				init_array();
				last->length = first->length;
			}
			else {ptr->length = first->length;first = ptr;}
			first->next = last;
			last->prev  = first;
			++*(first->length);

		}
		else{
			first = new d_link_exp<X >(x,index);
			last = first;
			size = new u_int(first->index+1);
			init_array();
		}
		set_array();
#else
		d_linked_exp_list<X >* ths =const_cast<d_linked_exp_list<X >* >(this);
		iterator& i = ths->find(index);
		if(i!=ths->end()) i.insert(x,index);
		else {
			if(first==0||(first&&first->idx()>=index)) ths->begin().insert(x,index);
			else if(last&&last->idx()<=index) ths->rbegin().insert(x,index);
		}
#endif
	}
	/**
	 * Auxiliary member
	 * Removes the list entry at
	 * position <code>index</code>
	 * @param index of list entry to remove
	 * @return true on success
	 */
	bool remove(u_int index){
		if(entry_array){
			if(index<size){
				d_link_exp<X >* ptr = entry_array[index];
				if(ptr) {
					if(ptr==last) {
						if(last->prev){
							last->prev->next = 0;
							last = last->prev;
							ptr->prev = 0;
						}
						else last = 0;
					}
					else if(ptr==first){
						if(first->next){
							first->next->prev = 0;
							first = first->next;
							ptr->next = 0;
						}
					}
					else {
						ptr->prev->next = ptr->next;
						ptr->next->prev = ptr->prev;
						ptr->prev = 0;
						ptr->next = 0;
					}
					delete ptr;
					ptr = 0;
					entry_array[index] = 0;
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @brief Auxiliary member
	 *
	 * Initializes the array view of
	 * this list
	 */
	void set_array(){
		if(entry_array==0) return;
		d_link_exp<X >* ptr = first;
		while(ptr&&ptr->index<size) {
			entry_array[ptr->index] = ptr;
			ptr = ptr->next;
		}
		if(ptr&&ptr->index>=size)
			std::cerr << "Size violation!\nmax_size = " << size << "last index" << ptr->index+1 <<"\n";
	}
	/**
	 * @brief Auxiliary member
	 *
	 * Clears all entries from this list and copies
	 * all entries from <code>list</code> in the same order
	 * <b>Note</b>, the list is left unchanged if either a mutable
	 * or constant iterator is still attached to the current list view
	 */
	void set_list(const d_link_exp<X >& list){
		if(has_const_iterator||has_muta_iterator){
			return;
		}
		if(entry_array!=0){
			del_array();
			delete first;
			size=*list.length;
			init_array();
		}
		first = new d_link_exp<X >(*list,list.index);
		entry_array[first->index] = first;
		d_link_exp<X >* ptr0 = list.next,* ptr1 = first;
		while(ptr0) {
			ptr1 = ptr1->add(ptr0->operator*(),ptr0->index);
			entry_array[ptr1->index] = ptr1;
			ptr0 = ptr0->next;
		}
		last = ptr1;
	}
	/**
	 * @brief Default constructor
	 */
	d_linked_exp_list():
		entry_array(0),
		first(0),last(0),
		size(0),has_const_iterator(0),has_muta_iterator(0){}
	/**
	 * @brief Range constructor
	 *
	 * Constructs a list of items returned by input iterator <code>i</code>.
	 * The type <code>INPUT_ITER</code> has to provide an increment and a
	 * pointer operator: <code>const X* operator->() const ;</code>
	 * <p><b>Note</b>, if <code>INPUT_ITER</code> is equal to
	 * <code>const_iterator</code> or <code>iterator</code>, the positions
	 * are kept. In case of subclasses, upcast the instances appropriately
	 * @param i input iterator
	 * @param e terminal iterator
	 */
	template<class INPUT_ITER >
	explicit d_linked_exp_list(INPUT_ITER& i, const INPUT_ITER& e):
		entry_array(0),
		first(0),last(0),
		size(0),has_const_iterator(0),has_muta_iterator(0){

		if(i!=e){
			iterator ii = begin();
			u_int idx;
			while(i!=e){
#if ((INPUT_ITER==const_iterator)||(INPUT_ITER==iterator))
				const d_link_exp<X >* lnk = i.operator->();
				if(lnk){
					ii.insert(lnk->operator*(),lnk->idx(),true);
				}

#else
				const X* x_ptr = i.operator->();
				if (x_ptr) {
					ii.insert(*x_ptr,idx,true);
					++idx;
				}
#endif
				++i;
			}
			set_array();
		}
	}
	/**
	 *
	 */
	d_linked_exp_list(X* i, X* e, u_int idx_hint = 0):
		entry_array(0),
		first(0),last(0),
		size(0),has_const_iterator(0),has_muta_iterator(0){
		if(i!=e){
			u_int count(idx_hint);
			iterator ii = begin();
			while(i!=e){
				ii.insert(*i, count,true);
				++i; ++count;
			}
			const u_int& lst_idx = ii->idx() + 1;
			if(size!=count)
				size = count;
			init_array();
			set_array();
		}
	}
public:
	/**
	 * @brief Copy constructor
	 * @param o original
	 */
	d_linked_exp_list(const d_linked_exp_list<X >& o):
			entry_array(0),
			first(0),last(0),
			size(o.size),has_const_iterator(0),
			has_muta_iterator(0)
		{
			if(o.first){
				init_array();
				set_list(*o);
			}

		}

	/**
	 * Destructor
	 */
	virtual ~d_linked_exp_list(){
		del_array();
		if(first){
			delete first;
			first = 0;
			last = 0;
		}
		if(has_const_iterator) {
			std::cout << "util::d_linked_exp_list<X >::const_iterator instance still alive - deleting\n";
			if(has_const_iterator->child) {delete has_const_iterator->child;has_const_iterator->child = 0;}
			delete has_const_iterator;
			has_const_iterator = 0;
		}
		if(has_muta_iterator) {
			std::cout << "util::d_linked_exp_list<X >::iterator instance still alive - deleting\n";
			if(has_muta_iterator->child) {delete has_muta_iterator->child;has_muta_iterator->child = 0;}
			delete has_muta_iterator;
			has_muta_iterator = 0;
		}
	}
	/**
	 * The empty list entry reference
	 */
	static const d_link_exp<X >& EMPTY_LIST;
	/**
	 * @brief The constant iterator class
	 *
	 * Provides basic traversal operations for the
	 * <code>d_linked_exp_list</code> in both directions
	 * Note, except for the terminal iterator, each copying
	 * creates a child iterator referencing its parent.
	 * <p>As these kinds of iterators do not alter the
	 * underlying list, multiple instantiations may exist simultaneously.
	 * <p>As a lightweight object, it does not own its list and
	 * therefore must never be dereferenced when its list is out of scope:
	 * <pre>class some_interesting_type //...
	 * typedef d_linked_exp_list<some_interesting_type > i_type_list;
	 *
	 * i_type_list::const_iterator create_n_destroy_list_with_iter(
	 * const some_interesting_type& value){//an erroneous function...
	 *    i_type_list list;//Create a list locally...
	 *    //fill list and create and iterator:
	 *    i_type_list::const_iterator i = list.begin();//or find()..., are still OK!
	 *    // maybe some further manipulation of the list via iterators and then:
	 *    return i;}//error: 'list' will be out of scope after return leaving
	 *    //list pointer of the returned iterator 'dangling'
	 *  </pre>
	 *  Furthermore, terminal iterators can never be transformed to non-terminals,
	 *  i.e.
	 *  <pre>d_linked_exp_list<some_interesting_type > l = ...;</pre>
	 *
	 */
	class const_iterator : public std::iterator<std::random_access_iterator_tag,X >{
		/**A pointer to the underlying list
		 * instance*/
		const d_linked_exp_list<X >* list;
		/**Pointer to parent iterators
		 * attached to the same list*/
		mutable const const_iterator* parent,
		/**Pointer to child iterators attached
		 * to the same list*/
		* child;
		/**A pointer to the current list
		 * entry*/
		d_link_exp<X >* ptr;
		/**Reverse traversal flag*/
		bool reverse,
			/**End iterator flag*/
			end_it;
		/**
		 * @brief Main constructor
		 *
		 *
		 * Constructs an iterator pointing
		 * to either the head entry
		 * (normal directional iterator) or to the
		 * tail entry (reversed traversal) of the
		 * <code>list</code> object
		 * @param list a pointer to the list
		 * @param rev_flag reverse traversal flag, defaults to false
		 * @param is_end end iterator flag, defaults to false
		 */
		const_iterator(const d_linked_exp_list<X >* list_ptr, bool rev_flag = false,
				bool is_end = false):
			list(list_ptr),
			parent(0),child(0),
			ptr(0),reverse(rev_flag),
			end_it(is_end)
		{
			if(!end_it) {
				ptr = reverse?list->last:list->first;
				parent = list->has_const_iterator;
				if(parent){
					while(parent->child) parent = parent->child;
					parent->child = const_cast<const const_iterator*>(this);
				}
				//if(parent) parent->child = const_cast<const const_iterator*>(this);
				else list->has_const_iterator = const_cast<const const_iterator*>(this);
			}
		}
	public:
		/**
		 * @brief Copy constructor
		 * @param o original
		 */
		const_iterator(const const_iterator& o):
			list(o.list),
			parent(0),child(0),ptr(0),
			reverse(o.reverse),
			end_it(o.end_it)
		{
			if(!end_it) {
				ptr = reverse?list->last:list->first;
				parent = &o;
				const const_iterator* ptr = &o;
				while(ptr){parent = ptr; ptr=ptr->child;}
				parent->child = this;
			}
		}
		/**
		 * @brief Destructor
		 */
		~const_iterator(){
			if(!parent&&!child) {
				if(list->has_const_iterator==this) list->has_const_iterator = 0;
			}
			else if(!parent&&child){
				list->has_const_iterator = child;
				child->parent = 0;
				child = 0;
			}
			else if(parent&&!child){
				parent->child = 0;
				parent = 0;
			}
			else {
				parent->child = child;
				child->parent = parent;
				parent = 0; child = 0;
			}
		}
		/**
		 * @brief Assignment operator
		 * @param i some iterator
		 * @return this iterator pointing to a new location
		 * in a new list
		 */
		const_iterator& operator=(const const_iterator& i) {
			if(list==i.list){
				if(ptr==i.ptr) return *this;
				ptr = i.ptr;
			}
			else{
				if(parent&&child){
					parent->child = child;
					child->parent = parent;
					child = 0;
				}
				else if(!parent&&child){
					list->has_const_iterator = child;
					child->parent = 0;
					child = 0;
				}
				else if(!child&&parent){
					parent->child = 0;
				}
				else {
					if(list->has_const_iterator==this) list->has_const_iterator = 0;
				}
				if(!i.end_it){
					const const_iterator* ptrr = i.child;
					if(ptrr){
						while(ptrr){parent=ptrr;ptrr=ptrr->child;}
					}
					else parent = &i;
					parent->child = const_cast<const const_iterator*> (this);
					list=i.list;
					ptr = i.ptr;
					reverse = i.reverse;
				}
				else end_it = true;
			}
			return *this;
		}
		/**
		 * @brief Dereference operator
		 * @return a constant reference to the current
		 * 		list entry in the list or <code>d_linked_exp_list<X >::EMPTY_LIST</code>
		 * 		if no more entries or end iterator
		 */
		const d_link_exp<X >& operator*() const {return ptr?*ptr:EMPTY_LIST;}
		/**
		 * @brief Pointer operator
		 * @return a pointer to <code>const d_link_exp&ltX&gt</code>
		 */
		const d_link_exp<X >* operator->() const {return ptr;}
		/**
		 * @brief Pointer operator
		 * @return a pointer to <code>d_link_exp&ltX&gt</code>
		 */
		d_link_exp<X >* operator->() {return ptr;}
		/**
		 * @brief Prefixed increment operator
		 * @return this iterator pointing to the next list
		 * 		entry or to null
		 */
		const_iterator& operator++() {
			if(!ptr) return *this;
			if(reverse){
				if(ptr->prev) ptr = ptr->prev;
				else {ptr = 0;end_it = true;}
			}
			else {
				if(ptr->next) ptr = ptr->next;
				else {ptr = 0;end_it = true;}
			}
			return *this;
		}
		/**
		 * @brief Postfixed increment operator
		 * @return as operator++()
		 */
		const_iterator& operator++(int dummy){
			return operator++();
		}
		/**
		 * @brief Equality operator member
		 *
		 * If one is terminal, the other not but with empty list, both
		 * are considered equal
		 * @param i1 some iterator
		 * @return true only if both are pointing to the same list AND the same
		 * 		current list entry AND the same directional flag
		 */
		bool operator==(const const_iterator& i1) const {
			if(i1.end_it&&end_it) return true;
			if(!i1.end_it&&!end_it){
				if(i1.list==list) {
					return i1.reverse==reverse&&i1.ptr==ptr;
				}
			}
			return !i1.end_it&&end_it&&i1.list&&i1.list->first==0?true:
					!end_it&&i1.end_it&&list&&list->first==0?true:false;
		}
		/**
		 * @brief Inequality operator
		 * @param i1 first iterator
		 * @param i2 second iterator
		 * @return complement of i1.operator==(i2)
		 */
		friend bool operator!=(const const_iterator& i1, const const_iterator& i2){
			return !(i1==i2);
		}
		friend class d_linked_exp_list<X >;
		friend class iterator;
	};
	/**
	 * @brief Mutable iterator class
	 *
	 * Each instance may erase entries from the underlying list
	 * whenever no other iterator is still attached to the list. To specify, removal
	 * of an entry which still has an iterator pointing to it is prohibitive
	 * and will throw a concurrent modification exception of type <code>concurr_except</code>
	 * by default. Only defining the <code>_D_LINKED_EXP_LIST_NO_THROW</code> macro
	 * will not throw anything - leaving the list unaltered. However, one is strongly
	 * urged not to rely on this implementation as a future version may make the exception
	 * mandatory.
	 * <p>Furthermore, as <code>const_iterator</code> any instance is a light weight object
	 * and therefore, does not own its underlying list structure. Using an iterator when its
	 * list is out of scope is erroneous.
	 */
	class iterator : public std::iterator<std::forward_iterator_tag,X> {
		d_linked_exp_list<X >* list;
		mutable iterator* parent,* child;
		/**A pointer to the current list
		 * entry*/
		d_link_exp<X >* ptr;
		/**Reverse traversal flag*/
		bool reverse,
		/**End iterator flag*/
		end_it;
		iterator(d_linked_exp_list<X >& lst, bool revFlag = false, bool flag = false):
			list(flag?0:&lst),parent(0),child(0),ptr(flag?0:revFlag?lst.last:lst.first),reverse(revFlag),end_it(flag){
			if(!end_it){
				iterator* i = list->has_muta_iterator;

				if(i) {
					while(i->child) i = i->child;
					i->child = this;
					parent = i;
				}
				else list->has_muta_iterator = this;
			}
		}
	public:
		iterator(const iterator& o):
			list(o.list),parent(0),child(0),ptr(o.ptr),reverse(o.reverse),end_it(o.end_it){
			iterator* i = o.list->has_muta_iterator;
			while(i->child) {
				i = i->child;
			}
			parent = i;
			parent->child = this;
		}
		virtual ~iterator(){
			if(!parent&&!child){
				if(list){
					list->has_muta_iterator = 0;
					if(list->size){
						if((list->size)<list->last->index+1){
							list->del_array();
							list->size=list->last->index+1;
						}
						list->init_array();
						list->set_array();

					}
				}
				//list->set_array();
			}
			else if(!parent&&child){
				list->has_muta_iterator = child;
				child->parent = 0;
				child = 0;
			}
			else if(parent&&!child){
				parent->child = 0;
				parent = 0;
			}
			else {
				parent->child = child;
				child->parent = parent;
				parent = child = 0;
			}
			if(ptr) ptr = 0;
			if(list) list = 0;
		}
		/**
		 * @brief Mutator member
		 *
		 * Inserts <code>x</code> at position <code>idx_hint</code>
		 * where <code>idx_hint</code> defaults to \f$2^{31} - 1\f$
		 * @param x instance to insert
		 * @param idx_hint position index, defaults to \f$2^{31} - 1\f$
		 * @param suppress_array suppresses array rearrangement - use true for multiple inserts repeatedly
		 * 		increasing the size to avoid excessive reallocations (defaults to false)
		 */
		void insert(const X& x, u_int idx_hint = -1u, bool suppress_array = false){
			if(end_it) return;
			if(ptr){
				if(idx_hint==-1u) idx_hint = 0;
				if(ptr->index==idx_hint) *(ptr->ptr) = x;
				else {
					if(ptr->index>idx_hint) {
						if(ptr->prev) {ptr = ptr->prev;insert(x,idx_hint,true);}
						else{
							ptr->prev = new d_link_exp<X >;
							ptr->prev->ptr = new X(x);
							ptr->prev->next = ptr;
							list->first = ptr->prev;
							ptr->prev->length = ptr->length;
							++(*ptr->length);
							if(idx_hint!=0) ptr->prev->index = idx_hint;
						}
					}
					else {
						if(ptr->next){
							if(ptr->next->idx()<=idx_hint) {
								ptr = ptr->next;
								insert(x,idx_hint,true);
							}
							else {
								d_link_exp<X >* ptr0 = new d_link_exp<X >;
								ptr0->ptr = new X(x);
								if(idx_hint!=0) ptr0->index = idx_hint;
								ptr0->prev = ptr;
								ptr0->next = ptr->next;
								ptr->next->prev = ptr0;
								ptr->next = ptr0;
								ptr0->length = ptr->length;
								++(*ptr->length);
								ptr = ptr0;
							}
						}
						else {
							ptr->next =  new d_link_exp<X >;
							ptr->next->ptr = new X(x);
							if(idx_hint) {
								ptr->next->index = idx_hint;
								if(list->size<idx_hint+1) list->size = idx_hint+1;
							}
							ptr->next->prev = ptr;
							ptr->next->length = ptr->length;
							++(*ptr->length);
							list->last = ptr->next;
							ptr = ptr->next;
						}
					}
				}
			}
			else if(!end_it){
				if(!list->entry_array){
					if(list->size){
						if(list->size<idx_hint+1&&!suppress_array) {
							list->del_array();
							list->size = idx_hint+1;
						}
					}
					else list->size = idx_hint+1;
					if(!suppress_array) list->init_array();
				}
				if(!list->first){
					list->first = new d_link_exp<X >(x,idx_hint);
					list->last = list->first;
					if(!suppress_array) {
						list->size = idx_hint+1;
						list->init_array();
						list->entry_array[idx_hint] = list->first;
					}
					ptr = list->first;
				}

			}
		}
		/**
		 * @brief Erasure member
		 *
		 * Removes the current entry from the underlying list
		 * if and only if:
		 * <ol><li>this iterator is not terminal</li>
		 * <li>the underlying list has no other (constant/mutable) iterators attached
		 * to it pointing to the same entry</li></ol>
		 * Currently, this member has to alternative implementation forms
		 * <ol><li>if macro <code>_D_LINKED_EXP_LIST_NO_THROW</code> defined
		 * this member silently ignores removal attempts of entries with iterators
		 * attached</li>
		 * <li>by default, the member throws an <code>concurr_except</code> when
		 * attempting to remove an entry with iterator attached</li></ol>
		 * <b>Note</b> this behavior may change in future - do not rely on
		 * the 'no_throw' macro as it may get removed.
		 */
		void erase()
#ifdef _D_LINKED_EXP_LIST_NO_THROW
		{
#else
		throw(concurr_except){

#endif
			if(end_it) return;
			if(list->has_const_iterator||list->has_muta_iterator){
				const d_link_exp<X >* lnk = const_cast<const d_link_exp<X >* >(ptr);
				if(list->has_const_iterator){
					const const_iterator* citer_ptr = list->has_const_iterator;
					while(citer_ptr){
						if(citer_ptr->operator->()==lnk)
#ifdef _D_LINKED_EXP_LIST_NO_THROW
							return;
#else
							throw concurr_except("Removal of entry with iterator attached is prohibited!");
#endif
						citer_ptr = citer_ptr->child;
					}
				}
				else if(list->has_muta_iterator){
					iterator* iter_ptr = list->has_muta_iterator;
					while(iter_ptr){
						if(iter_ptr!=this&&iter_ptr->operator ->()==ptr)
#ifdef _D_LINKED_EXP_LIST_NO_THROW
							return;
#else
							throw concurr_except("Removal of entry with iterator attached is prohibited!");
#endif
						iter_ptr = iter_ptr->child;
					}
				}
			}
			if(ptr) {
				d_link_exp<X >* ptr0 = ptr->prev,* ptr1 = ptr->next;
				if(ptr0&&ptr1){
					ptr0->next = ptr1;
					ptr1->prev = ptr0;
					ptr->prev = 0;
					ptr->next = 0;
					--list->size;
					list->entry_array[ptr->index] = 0;
					ptr->length = 0;
					delete ptr;
					ptr = reverse?ptr0:ptr1;
				}
				else if(!ptr0&&ptr1){
					ptr1->prev = 0;
					ptr->next  = 0;
					if(ptr==list->first) list->first = ptr1;
					--list->size;
					ptr->length = 0;
					list->entry_array[ptr->index] = 0;
					delete ptr;
					if(reverse) {ptr = 0; end_it = true;}
					else ptr = ptr1;
				}
				else if(ptr0&&!ptr1){
					ptr0->next = 0;
					ptr->prev = 0;
					if(ptr==list->last) {
						list->last = ptr0;
						*ptr->length = ptr0->index+1;
					}
					--list->size;
					list->entry_array[ptr->index] = 0;
					ptr->length = 0;
					delete ptr;
					if(reverse) ptr = ptr0;
					else {
						ptr = 0;
						end_it = true;
					}
				}
				else {
					if(list->first==ptr){
						if(list->last==ptr) list->last = 0;
						list->first = 0;
						list->del_array();
						list->size = 0;
					}
					delete ptr;ptr = 0;end_it=true;
				}
			}
		}
		/**
		 * @brief Assignment operator
		 *
		 * Note, if the list pointers of both iterators
		 * are distinct <code>this</code> will get removed
		 * from its current list and transferred to <code>o</code>'s
		 * list as its latest child
		 * @param o iterator to assign
		 * @return newly assigned iterator pointing to the same entry
		 * 		as <code>o</code>
		 */
		iterator& operator=(const iterator& o) {
			if(this==&o) return *this;
			if(child&&parent) {
				child->parent = parent;
				parent->child = child;
				parent = child = 0;}
			else if(child&&!parent) {
				list->has_muta_iterator = child;
				child->parent = 0;child = 0;}
			else if(!child&&parent) {parent->child = 0;parent = 0;}
			if(list!=o.list) list = o.list;
			ptr = o.ptr;
			reverse = o.reverse;
			end_it  = o.end_it;
			if(!o.child) {
				parent = const_cast<iterator*>(&o);
			}
			else{
				parent = o.child;
				while(parent) parent = parent->child;
			}
			parent->child = this;
			return *this;
		}
		/**
		 * @brief Increment operator
		 * @return either this iterator pointing to the next entry
		 * 	or a terminal iterator
		 */
		iterator& operator++() {
			if(ptr) {
				if(ptr->next) ptr = ptr->next;
				else {ptr = 0; end_it = true;}
			}
			return *this;
		}
		/**
		 * @brief Decrement operator
		 * @return either an iterator pointing to the previous
		 * 	entry or terminal iterator
		 */
		iterator& operator--() {
			if(ptr) {
				if(ptr->prev) ptr = ptr->prev;
				else {ptr = 0; end_it = true;}
			}
			return *this;
		}
		/**
		 * @brief Pointer operator
		 * @return a pointer to the current entry (<code>util::d_link_exp</code>)
		 * 	or NULL if terminal
		 */
		const d_link_exp<X >* operator->() const {
			return ptr?const_cast<const d_link_exp<X >* >(ptr):0;
		}
		/**
		 * @brief Equality operator
		 * @param i1 second iterator to compare
		 * @return true if either terminal (independent of list), or
		 * 	both pointing to the same list and same entry
		 */
		bool operator==(const iterator& i1) const {
			if(i1.end_it&&end_it) return true;
			if(i1.list==list) return i1.ptr==ptr&&i1.reverse==reverse;
			return false;
		}
		/**
		 * @brief Inequality operator
		 * @param i1 left hand iterator
		 * @param i2 right hand iterator
		 * @return true only if <code>i1.operator==(i2)</code> returns false
		 */
		friend bool operator!=(const iterator& i1, const iterator& i2) {
			return !(i1==i2);
		}
		friend class d_linked_exp_list<X >;
	};
	friend class const_iterator;
	friend class iterator;
	/**
	 * @brief Constant iterator member
	 * @return an iterator pointing to the head
	 */
	const_iterator begin() const {
		return const_iterator(this);
	}
	/**
	 * @brief Reverse constant iterator member
	 * @return a reversed iterator pointing to the tail
	 */
	const_iterator rbegin() const {
		return const_iterator(this,true);
	}
	/**
	 * @brief Mutable iterator member
	 * @return a mutable iterator pointing to head of the list
	 */
	iterator begin() {
		return iterator(*this);
	}
	/**
	 * @brief Reverse mutable iterator member
	 * @return a mutable iterator pointing to tail of the list
	 */
	iterator rbegin(){
		return iterator(*this,true);
	}
	/**
	 * @brief Terminal constant iterator
	 * @return the end iterator to <code>begin()</code>
	 */
	const_iterator end() const {
		return const_iterator(this,false,true);
	}
	/**
	 * @brief Reverse terminal mutable iterator
	 * @return the end iterator to <code>rbegin()</code>
	 */
	const_iterator rend() const {
		return const_iterator(this,true,true);
	}
	/**
	 * @brief Terminal iterator
	 * @return the end iterator to <code>iterator begin(), iterator rbegin()</code>
	 */
	iterator end() {return iterator(*this,false,true);}
	/**
	 * @brief Search member
	 * <p>Returns an iterator pointing to the entry with the same
	 * index or a terminal iterator
	 * @param idx index
	 * @return constant iterator
	 */
	const_iterator find(u_int idx) const {
		if(entry_array) {
			if(idx<size&&entry_array[idx]) {
				const_iterator i = begin();
				i.ptr = entry_array[idx];
				if(i.ptr) return i;
			}
		}
		return end();
	}
	/**
	 * @brief Search member
	 * <p>Returns a constant iterator pointing to the
	 * first entry in this list which equals <code>x</code>
	 * or 'past-the-end' iterator if not found
	 * @param x entry to look up
	 * @return iterator
	 */
	const_iterator find(const X& x) const {
		if(entry_array){
			const_iterator i = begin(), e = end();
			while(i!=e) {
				if(*(*i)==x) return i;
				++i;
			}
		}
		return end();
	}
	/**
	 * @brief Search member
	 * <p>Returns a (mutable) iterator pointing to the
	 * entry associated to index <code>idx</code> or an
	 * iterator pointing to an entry closest to the index:
	 * <pre>
	 * u_int mini(-1u);
	 * iterator i, e = end();
	 * for ( i = begin(); i!=e; ++i) {
	 * 		mini = min<u_int> (mini, i->idx());
	 * 	}
	 * bool test = mini == find(idx)->idx();//true
	 * </pre>
	 * @param idx index
	 * @return mutable iterator
	 */
	iterator find(u_int idx) {
		if(entry_array){
			if(!first||(first&&idx<=first->idx())) {
				return begin();
			}
			if(last&&idx>=last->idx()){
				return rbegin();
			}
			if(idx<size&&entry_array[idx]){
				iterator i = begin();
				i.ptr = entry_array[idx];
				if(i.ptr) return i;
			}
			iterator i1 = begin(), i2 = rbegin(), e = end();
			while (i1!=e||i2!=e){
				if(i1!=e) {
					if(i1->idx()<idx) ++i1;
					else break;
				}
				if(i2!=e){
					if(i2->idx()>idx) --i2;
					else break;
				}
				if(i1->idx()>=i2->idx()) break;
			}
			if(i1!=e) return i1;
			if(i2!=e) return i2;
		}
		return end();
	}
	/**
	 * @brief Search member
	 *
	 * Performs search of <code>val</code>
	 * from both ends (i.e. like calling <code>iterator d_linked_exp_list::begin()</code>
	 * and <code>iterator d_linked_exp_list::rbegin()</code> and returns either the
	 * first iterator pointing to an entry equal to <code>val</code> or terminal
	 * iterator if not found.
	 * @param val value to look-up
	 * @return (mutable) iterator
	 */
	iterator find(const X& val){
		iterator i1 = begin(), i2 = rbegin(), e = end();
		while(i1!=e||i2!=e) {
			if(i1!=e){
				if(*(*i1.operator->())==val) return i1;
				++i1;
			}
			if(i2!=e){
				if(*(*i2.operator->())==val) return i2;
				--i2;
			}
			if(i1.operator ->()==i2.operator ->()) break;
		}
		return e;
	}

	/**
	 * Assignment operator
	 * @param o the original instance
	 * @return the altered list
	 */
	d_linked_exp_list<X >& operator=(const d_linked_exp_list<X >& o){
		if(this==&o) return *this;
		if(o.size&&size){
			if(size<o.size) {
				del_array();
				size = o.size;
				init_array();
			}
			*first=*o.first;
			set_array();
		}
		else if(!o.size&&size) {
			del_array();
			delete first;
			first = 0;
			last = 0;
		}
		else if(o.size&&!size){
			size = o.size;
			init_array();
			set_list(*o);
		}
		return *this;
	}
	/**
	 * Dereference operator
	 * @returns either the reference of the head or of
	 * 		the <code>EMPTY_LIST</code>
	 */
	const d_link_exp<X >& operator*() const {return first?*first:EMPTY_LIST;}
	/**
	 * @brief Subscript operator
	 * @param idx unsigned integer index
	 * @return a const pointer to the entry if existent or null, if
	 * 		either out of bounds or no entry
	 */
	const d_link_exp<X >* operator[](u_int idx) const {
		return first?
				idx<*first->length?
						entry_array[idx]:0:
						0;
	}
	/**
	 * @brief Maximal index member
	 * @return greatest index <code>max</code> in array view, such
	 * 		all indices <code>0 <= i && i < max</code> are legitime
	 * 		array entry (though may be null)
	 */
	u_int max_index() const {
		return first?*first->length:0;
	}
	/**
	 * @brief Inversion member
	 * @return the same list with entry order reverted
	 */
	d_linked_exp_list<X > reverse() const {
		if(size==0||first==0) return *this;
		d_linked_exp_list<X > rev;
		rev.size = size;
		iterator ii = rev.begin();
		const_iterator i = begin(), e = end();
		const u_int& sz = size-1u;
		while(i!=e) {
			const d_link_exp<X >* lnk = i.operator->();
			ii.insert(lnk->operator*(),sz-lnk->idx());
			++i;
		}
		return rev;
	}
	friend class d_link_exp<X >;
};
/*
 * Class member data initialization
 */
template<class X >
const d_link_exp<X >& d_linked_exp_list<X >::EMPTY_LIST = d_link_exp<X >();

/**
 * Inequality operator
 * @param i1 first iterator
 * @param i2 second iterator
 * @return <code>!operator==(i1,i2)</code>
 */
template<class X >
bool operator!=(const typename d_linked_exp_list<X >::const_iterator& i1,
		const typename d_linked_exp_list<X >::const_iterator& i2){
	return i1==i2?false:true;
}
#endif /* D_LINK_LIST_H01_UNSIZED_D_LINKS */

}//end namespace 'util'

#ifdef D_LINK_LIST_H01_CONST_REF_SPEC
#include "d_link_exp_c_ref.hpp"
#endif

#endif /* D_LINK_LIST_H01_ */
