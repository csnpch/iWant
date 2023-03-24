package com.ituy.iwant.Services

import com.ituy.iwant.Models.MemberModel

class MemberService {

    fun getMemberByID(memberID: Int): MemberModel {
        return MemberModel()
    }

    fun updateInfo(memberModel: MemberModel): MemberModel {
        return memberModel
    }

}