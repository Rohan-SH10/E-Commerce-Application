import React from 'react'

const Button = ({text,onClick}) => {
  return (
    <button
        type='button'
        onClick={onClick}
        className='px-8 py-2 my-4 rounded-md text-white bg-blue-600 w-full text-xl font-semibold'
        >
        {text}
    </button>
  )
}
export default Button