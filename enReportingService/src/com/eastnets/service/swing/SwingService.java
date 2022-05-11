/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.swing;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.domain.swing.SwingEntity;

/**
 * Swing Service Interface
 * @author EastNets
 * @since September 3, 2012
 */
@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public interface SwingService {

/**
 * Do search within swing tables 
 * @param loggedInUser
 * @param swing
 * @return ArrayList<SwingEntity>
 */
public ArrayList<SwingEntity> doSearch(String loggedInUser, SwingEntity swing);

/**
 * View message details
 * @param loggedInUser
 * @param statusId
 * @param uniqueIdent
 * @return SwingEntity
 */
public SwingEntity viewMessageDetails(String loggedInUser, String statusId, String uniqueIdent);

}
