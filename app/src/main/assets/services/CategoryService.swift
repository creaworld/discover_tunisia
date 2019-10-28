//
//  CategoryService.swift
//  trid
//
//  Created by Black on 9/30/16.
//  Copyright © 2016 Black. All rights reserved.
//

import UIKit
import Firebase
import FirebaseDatabase
import FirebaseStorage

class CategoryService: NSObject {
    // Path to table
    static let path = "category_info"
    // Singleton
    static let shared = CategoryService()
    
    // ref
    var ref : FIRDatabaseReference!
    // data
    var categories : [FCategory] = []
    
    // init
    override init() {
        super.init()
    }
    
    public func configureDatabase(finish: @escaping () -> Void) {
        // remove old data
        categories.removeAll()
        // ref
        ref = FIRDatabase.database().reference(withPath: CategoryService.path)
        ref.keepSynced(true)
        // remove all observe
        ref.removeAllObservers()
        // added
        ref.observe(.childAdded, with: {snapshot in
            let cat = FCategory(path: CategoryService.path, snapshot: snapshot)
            debugPrint("Category Priority = ", snapshot, cat.getPriority())
            self.categories.append(cat)
        })
        ref.observe(.value, with: {snapshot in
            debugPrint("DONE Category")
            finish()
        })
    }
    
    public func getCategoryType(fromKey key: String) -> FCategoryType? {
        let cat = categories.first(where: {$0.snapshot?.key == key})
        if cat != nil {
            return cat?.getType()
        }
        return nil
    }
    
    func getCategoryKeyForType(_ type: FCategoryType) -> String? {
        for cat in categories {
            if cat.objectId != nil && cat.getType() == type {
                return cat.objectId!
            }
        }
        return nil
    }
}
