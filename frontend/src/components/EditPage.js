import { Button, Divider, Flex, Heading, Input, InputGroup, Select, VStack } from '@chakra-ui/react'
import React, { useEffect, useMemo, useRef, useState } from 'react'
import JoditEditor from 'jodit-react';
import categoryService from "../services/category-services"
import postServices from '../services/post-services';
import { useNavigate, useParams } from 'react-router-dom';
import authServices from '../services/auth-services';

export const EditPage = () => {

    const { id } = useParams()

    const config = useMemo(
        () => ({
            readonly: false,
            placeholder: 'Start typings...',
            style: { color: 'black' }
        }),
        []
    );

    const editor = useRef(null);
    const [categoryList, setCategoryList] = useState('')
    const [title, setTitle] = useState()
    const [content, setContent] = useState()
    const [category, setCategory] = useState()
    const [data, setData] = useState()
    const [image, setImage] = useState('')

    useEffect(() => {
        postServices.getPostById(id).then(resp => {
            setTitle(resp.title)
            setContent(resp.content)
            setData(resp)
        })
    }, [])

    useEffect(() => {
        categoryService.getCategories().then(resp => setCategoryList(resp))
    }, [])

    const navigate = useNavigate()


    const onSubmit = (e) => {
        e.preventDefault()

        const newData = {
            title: title,
            content: content
        }

        if (image != null) {
            postServices.updatePostById(id, newData).then(() => {
                postServices.addNewImage(id, image)
                navigate('/')
            })
        }

    }

    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>Edit your post</Heading>
                <Divider width={"50%"} />
            </VStack>
            {data ?
                <form onSubmit={onSubmit}>
                    <Flex justify={"center"}>
                        <VStack width="33%">
                            <InputGroup>
                                <Input defaultValue={title} onChange={(e) => setTitle(e.target.value)} required name="title" type="text" placeholder='Post title' />
                            </InputGroup>
                            <InputGroup>
                                <Select value={category} onChange={(e) => setCategory(e.target.value)} required placeholder='Select a post category'>
                                    {categoryList ? categoryList.map((item, i) => {
                                        return <option key={i}>{item.name}</option>
                                    }) : ""}
                                </Select>
                            </InputGroup>
                            <InputGroup>
                                <Input onChange={(e) => setImage(e.target.files[0])} type="file" accept="image/png, image/gif, image/jpeg" />
                            </InputGroup>
                            <JoditEditor
                                ref={editor}
                                value={content}
                                config={config}
                                tabIndex={1}
                                onBlur={newContent => setContent(newContent)}
                            //onChange={newContent => setContent(newContent)}
                            />
                            <Button type="submit">Edit your post</Button>
                        </VStack>
                    </Flex>
                </form>
                : ""}
        </>
    )
}
