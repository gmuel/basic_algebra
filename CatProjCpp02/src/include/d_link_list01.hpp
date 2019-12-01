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

#include "alg/1.1/utils/utils_d_link_list01.hpp"

#endif /* D_LINK_LIST_H01_ */
