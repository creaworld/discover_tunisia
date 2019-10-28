//
//  TridService.swift
//  trid
//
//  Created by Black on 9/28/16.
//  Copyright © 2016 Black. All rights reserved.
//

import UIKit
import Firebase
import FirebaseDatabase
import FirebaseStorage

class TridService: NSObject {
    // ref
    var storageRef: FIRStorageReference!
    
    // Singleton
    static let shared = TridService()
    
    // Variable
    var isOnline = false
    var listenConnectionCheck : ((Bool) -> Void)?
    
    // init
    override init() {
        super.init()
        checkConnection()
    }
    
    func checkConnection() {
        let connectedRef = FIRDatabase.database().reference(withPath: ".info/connected")
        connectedRef.observe(.value, with: { snapshot in
            let connected = snapshot.value as? Bool ?? false
            self.isOnline = connected
            if self.listenConnectionCheck != nil {
                self.listenConnectionCheck!(connected)
            }
            debugPrint(connected ? "Connected" : "Not connected")
        })
    }
    
    public func makeFirebase(finish: @escaping () -> Void){
        var _progress = 0
        var isdone = false
        let totalProgress = 5
        let handlefinish = {() -> Void in
            _progress += 1
            if _progress >= totalProgress && !isdone{
                isdone = true
                finish()
            }
        }
        // load language -> country -> city
        LanguageService.shared.configureDatabase(finish: {() -> Void in
            handlefinish()
        })
        // load category
        CategoryService.shared.configureDatabase(finish: {() -> Void in
            handlefinish()
        })
        // load sub-category
        SubCategoryService.shared.configureDatabase(finish: {() -> Void in
            handlefinish()
        })
        // load facility
        FacilityService.shared.configureDatabase(finish: {() -> Void in
            handlefinish()
        })
        // load all user
        UserService.shared.configureDatabase(finish: {
            handlefinish()
        })
        
        // config storage
        configureStorage()
    }
    
    private func configureStorage() {
        storageRef = FIRStorage.storage().reference(forURL: "gs://" + (FIRApp.defaultApp()?.options.storageBucket)!) // trid-admin.appspot.com
    }
    
    
}
